import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { MatDialog, PageEvent } from '@angular/material';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service'
import { HotelReserveDialogComponent } from '../hotel-reserve-dialog/hotel-reserve-dialog.component';
import { AddEntityDialogComponent } from '../add-entity-dialog/add-entity-dialog.component';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css']
})
export class HotelComponent implements OnInit {

  hotel = {};
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

  openReserveDialog(id: number){
    const dialogRef = this.dialog.open(HotelReserveDialogComponent,
      {
        data: id,
        disableClose: true,
        autoFocus: true,
        height: '90%',
        width: '50%',
      });
  }

}
