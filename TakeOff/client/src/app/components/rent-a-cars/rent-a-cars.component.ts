import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { PageEvent, MatDialog, MatSnackBar, MatSelect } from '@angular/material';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';
import { AppComponent } from 'src/app/app.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { VehicleReservationDialogComponent } from '../vehicle-reservation-dialog/vehicle-reservation-dialog.component';
import { AddEntityDialogComponent } from '../add-entity-dialog/add-entity-dialog.component';
import { ReservationService } from 'src/app/services/reservation/reservation.service';

@Component({
  selector: 'app-rent-a-cars',
  templateUrl: './rent-a-cars.component.html',
  styleUrls: ['./rent-a-cars.component.css']
})
export class RentACarsComponent implements OnInit {

  rentACars = [];
  pageSize = 10;
  pageIndex = 0;
  message: string;
  userRole: string;
  loading = true;
  sortOptions = [{value: 'nameAsc', viewValue: 'Name', icon: 'arrow_upward', sortBy: 'name', asc: true},
                 {value: 'nameDesc', viewValue: 'Name', icon: 'arrow_downward', sortBy: 'name', asc: false},
                 {value: 'ratingAsc', viewValue: 'Rating', icon: 'arrow_upward', sortBy: 'rating', asc: true},
                 {value: 'ratingDesc', viewValue: 'Rating', icon: 'arrow_downward', sortBy: 'rating', asc: false}];
  selected = this.sortOptions[0].value;
  selectedOption = this.sortOptions[0];
  nameFilterParam = '';
  countryFilterParam = '';
  cityFilterParam = '';

  constructor(private authService: AuthenticationService,
              private rentACarService: RentACarService,
              private reservationService: ReservationService,
              public dialog: MatDialog,
              public appComponent: AppComponent) { }

  ngOnInit() {
    this.rentACarService.getRentACars().subscribe(
      (data: []) => {
        this.rentACars = data;
        this.userRole = this.authService.getAuthority();
        this.rentACarService.getRentACarRatings().subscribe(
          (ratings) => {
            // tslint:disable-next-line:forin
            for (const i in this.rentACars) {
                this.rentACars[i].rating = ratings[i];
                // this.rentACars[i].ratingToShow = ratings[i];
            }
            this.rentACars = this.appComponent.sortArray(data, 'name', true);
            this.loading = false;
          }
        );
      }
    );

    this.authService.onSubject.subscribe(
      (data) => {
        this.userRole = this.authService.getAuthority();
      }
    );
  }

  // showRating(index: number) {
  //   this.rentACars[index].ratingToShow = this.rentACars[index].rating;
  // }

  // hoverRating(index: number, rating: number) {
  //   this.rentACars[index].ratingToShow = rating;
  // }

  openAddRentACarDialog() {
    const dialogRef = this.dialog.open(AddEntityDialogComponent,
    {
      data: 2,
      disableClose: true,
      autoFocus: true,
      width: '60%',
      height: '90%'
    });
    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {
            this.message = 'Added successfully!';
            const newRentACar = result;
            newRentACar.rating = 0;
            this.rentACars.push(newRentACar);
            this.rentACars = this.appComponent.sortArray(this.rentACars, this.selectedOption.sortBy, this.selectedOption.asc);
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

  switchPage(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
  }

  sort(value: any) {
    for (const i in this.sortOptions) {
      if (this.sortOptions[i].value === value) {
        this.selectedOption = this.sortOptions[i];
      }
    }
    this.rentACars = this.appComponent.sortArray(this.rentACars, this.selectedOption.sortBy, this.selectedOption.asc);
  }

  search(searchName: string, countryParam: string, cityParam: string) {
    this.nameFilterParam = searchName;
    this.countryFilterParam = countryParam;
    this.cityFilterParam = cityParam;
  }

  openReservationDialog(rentACar: any, reservation: any) {
    const dialogRef = this.dialog.open(VehicleReservationDialogComponent,
    {
      data: {
        'rentACar': rentACar,
        'landingDate': new Date(reservation.ticket.flight.landingDate)
      },
      disableClose: true,
      autoFocus: true,
      width: '60%',
      height: '90%'
    });
    dialogRef.afterClosed().subscribe(
      (vehicleToReserve) => {
        if (vehicleToReserve) {
          this.finishReservationProcess(vehicleToReserve, reservation);
        }
      }
    );
  }

  beginReservationProcess(rentACar) {
    if (!this.authService.getUsername() || this.userRole !== 'ROLE_USER') {
      this.appComponent.showSnackBar('Error! You have no right to make any reservation.');
      return;
    }
    this.rentACarService.areThereAvailableVehiclesNotOnDiscount(rentACar.id).subscribe(
      (thereAreAvailableVehicles) => {
        if (thereAreAvailableVehicles) {
          this.reservationService.getReservations(this.authService.getUsername()).subscribe(
            (userReservations: any[]) => {
              const availableReservations = new Array();
              userReservations.forEach(reservation => {
                // find all active reservations in rent a cars location (just look for country and city)
                if (!reservation.reservationDTO.vehicleReservation) {
                  const landingDate = new Date(reservation.ticket.flight.landingDate);
                  const landingCountry = reservation.ticket.flight.landingLocation.country;
                  const landingCity = reservation.ticket.flight.landingLocation.city;
                  console.log(landingDate);
                  console.log(landingCountry);
                  console.log(landingCity);
                  if (landingDate.getTime() > new Date().getTime() &&
                      landingCountry === rentACar.location.country &&
                      landingCity === rentACar.location.city) {

                    availableReservations.push(reservation);
                  }
                }
              });
              if (availableReservations.length > 0) {
                let reservation = availableReservations[0];
                availableReservations.forEach(availableReservation => {
                  // find reservation which is nearest by the plane take off date
                  if (availableReservation.ticket.flight.landingDate < reservation.ticket.flight.landingDate) {
                    reservation = availableReservation;
                  }
                });
                console.log(reservation);
                this.openReservationDialog(rentACar, reservation);
              } else {
                this.appComponent.showSnackBar('You have no active flight reservation at the moment at the place you are looking for!');
              }
            }
          );
        } else  {
          this.appComponent.showSnackBar('There are no available any vehicle at the moment.');
        }
      }
    );
  }

  finishReservationProcess(vehicleToReserve: any, reservation: any) {
    if (!vehicleToReserve || !reservation) {
      this.appComponent.showSnackBar('Error! Something went wrong.');
      return;
    }

    const vehicleReservation = vehicleToReserve;
    vehicleReservation.reservationId = reservation.reservationDTO.id;
    console.log(vehicleReservation);
    this.reservationService.createVehicleReservation(vehicleReservation).subscribe(
      () => {
        this.appComponent.showSnackBar('You have created vehicle reservation successfully!');
      },
      () => {
        this.appComponent.showSnackBar('Error! Your reservation have not been made.');
      }
    );
  }
}
