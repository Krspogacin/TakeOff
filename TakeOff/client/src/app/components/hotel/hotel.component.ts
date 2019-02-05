import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AddHotelModalComponent } from 'src/app/components/add-hotel-modal/add-hotel-modal.component';
import { MatDialog, PageEvent } from '@angular/material';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service'
import { HotelReserveDialogComponent } from '../hotel-reserve-dialog/hotel-reserve-dialog.component';

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
  length = this.hotels.length;
  id = 0;
  userRole: string = null;

  constructor(private HotelService: HotelService, private authService: AuthenticationService, private route: ActivatedRoute,public dialog: MatDialog) { }

  ngOnInit() {
      this.HotelService.getHotels().subscribe(
      (data:any) => { 
        console.log(data); 
        this.hotels = data;
      });
      this.userRole = this.authService.getAuthority();
  }

  openDialog(){
    const dialogRef = this.dialog.open(AddHotelModalComponent,
    {
      disableClose: true,
      width: '60%',
      height: '90%'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Hotel:', result);
        this.hotels.push(result);
      }
    });
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
