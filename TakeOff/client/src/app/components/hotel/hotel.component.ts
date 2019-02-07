import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { MatDialog, PageEvent } from '@angular/material';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service'
import { HotelReserveDialogComponent } from '../hotel-reserve-dialog/hotel-reserve-dialog.component';
import { AddEntityDialogComponent } from '../add-entity-dialog/add-entity-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { ReservationService } from 'src/app/services/reservation/reservation.service';

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css']
})
export class HotelComponent implements OnInit {

  hotels = [];
  filteredHotels = [];
  pageSize = 10;
  pageIndex = 0;
  nameLocation = "";
  message: string;
  length = this.hotels.length;
  id = 0;
  userRole: string = null;
  hotelRatings: any;

  constructor(private HotelService: HotelService,
             private authService: AuthenticationService,
             public dialog: MatDialog,
             private reservationService: ReservationService,
             public appComponent: AppComponent) { }

  ngOnInit() {
      this.HotelService.getHotels().subscribe(
      (data:any) => { 
        this.hotels = data;
      });
      this.HotelService.getHotelRatings().subscribe(
        (data) => {
          this.hotelRatings = data;
        }
      );
      this.userRole = this.authService.getAuthority();
  }

  openDialog(){
    const dialogRef = this.dialog.open(AddEntityDialogComponent,
    {
      data: 1,
      disableClose: true,
      autoFocus: true,
      width: '60%',
      height: '90%'
    });

    dialogRef.afterClosed().subscribe(
    (result) => {
      if (result) {
        this.hotels.push(result);
        this.message = 'Added successfully!';
      }
    },
    () => {
      this.message = 'Error! Rent A Car could not be added!';
    },
    () => {
      this.appComponent.showSnackBar(this.message);
    }
    );
  }

  pageFunction(event : PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
  }

  search(){
    this.hotels.forEach(hotel => {
      if (hotel.name.indexOf(this.nameLocation) != -1){
        this.filteredHotels.push(hotel);
      }
    });
    this.hotels = this.filteredHotels;
  }
  openReservationDialog(hotel: any, reservation: any){
  const ladningDate = new Date(reservation.ticket.flight.landingDate);
  const dialogRef = this.dialog.open(HotelReserveDialogComponent,
    {
      data: {
        'hotel': hotel,
        'landingDate': ladningDate
      },
      disableClose: true,
      autoFocus: true,
      height: '90%',
      width: '50%',
    });
    dialogRef.afterClosed().subscribe(
      (sendingObject) => {
        if (sendingObject) {
          this.finishReservationProcess(sendingObject, reservation);
        }
      }
    )
  }

  beginReservation(hotel){
    if (!this.authService.getUsername() || this.userRole !== 'ROLE_USER') {
      this.appComponent.showSnackBar('Error! You have no right to make any reservation.');
      return;
    }
    this.HotelService.roomsNotOnDiscount(hotel.id).subscribe(
      (flag) => {
        if (flag) {
          this.reservationService.getReservations(this.authService.getUsername()).subscribe(
            (userReservations: any[]) => {
              const availableReservations = new Array();
              userReservations.forEach(reservation => {
                if (!reservation.reservationDTO.roomReservation) {
                  const landingDate = new Date(reservation.ticket.flight.landingDate);
                  const landingCountry = reservation.ticket.flight.landingLocation.country;
                  const landingCity = reservation.ticket.flight.landingLocation.city;
                  const tomorrow = new Date(landingDate);
                  tomorrow.setDate(tomorrow.getDate() + 1);
                  if (tomorrow.getTime() >=  new Date().getTime() &&
                      landingCountry === hotel.location.country &&
                      landingCity === hotel.location.city) {

                    availableReservations.push(reservation);
                  }
                }
              });
              if (availableReservations.length > 0) {
                let reservation = availableReservations[0];
                availableReservations.forEach(availableReservation => {
                  if (availableReservation.ticket.flight.landingDate < reservation.ticket.flight.landingDate) {
                    reservation = availableReservation;
                  }
                });
                console.log(reservation);
                this.openReservationDialog(hotel, reservation);
              } else {
                this.appComponent.showSnackBar('You have no active flight reservation at the moment at the place you are looking for!');
              }
            }
          );
        } else  {
          this.appComponent.showSnackBar('There are no available any rooms at the moment.');
        }
      }
    );
  }

  finishReservationProcess(roomToReserve: any, reservation: any) {
    if (!roomToReserve || !reservation) {
      this.appComponent.showSnackBar('Error! Something went wrong.');
      return;
    }

    const roomReservation = roomToReserve;
    roomReservation.reservationId = reservation.reservationDTO.id;
    console.log(roomReservation);
    this.reservationService.createRoomReservation(roomReservation).subscribe(
      () => {
        this.appComponent.showSnackBar('You have created room reservation successfully!');
      },
      () => {
        this.appComponent.showSnackBar('Error! Your reservation have not been made.');
      }
    );
  }
}
