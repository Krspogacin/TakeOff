import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AddHotelModalComponent } from 'src/app/components/add-hotel-modal/add-hotel-modal.component';
import {MatDialog} from '@angular/material';

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css']
})
export class HotelComponent implements OnInit {

  hotel = {};
  hotels = [];
  id = 0;

  constructor(private HotelService: HotelService, private route: ActivatedRoute,public dialog: MatDialog) { }

  ngOnInit() {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    console.log('id: ' + this.id);
    if (!isNaN(this.id)){
      this.HotelService.getHotelById(this.id).subscribe(
        (data) => {
          this.hotel = data;
        },
        error => {
          // handle not found error
        });
        
      }
      this.HotelService.getHotels().subscribe(
      (data:[]) => {  
        this.hotels = data;
      });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddHotelModalComponent, {
      width: '250px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.HotelService.addHotel(result).subscribe(
        (data:{}) => {
          this.hotels.push(data);
        }
      )
    });
  }

}
