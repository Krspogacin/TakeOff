import { Component, OnInit } from '@angular/core';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatSnackBar, PageEvent } from '@angular/material';
import { RoomService } from 'src/app/services/room/room.service';
import { HotelDialogComponent } from '../hotel-dialog/hotel-dialog.component';
import { RoomDialogComponent } from '../room-dialog/room-dialog.component';

@Component({
  selector: 'app-hotel-profile',
  templateUrl: './hotel-profile.component.html',
  styleUrls: ['./hotel-profile.component.css']
})
export class HotelProfileComponent implements OnInit {

  loadingHotel = true;
  loadingRooms = true;
  loadingPrices = true;
  hotelExists = false;
  hotel: {} = null;
  rooms: any = null;
  prices: any = null;
  roomPrices: any = null;
  message: string;
  userRole: string = null;
  pageSize = 10;
  pageIndex = 0;
  pageOptions = [5, 10, 25, 50];
  displayedColumns: string[] = ['type', 'numberOfBeds', 'floor','discount','hasBalcony', 'hasAirCondition',
                'numberOfRooms','priceFor5Days','priceFor10Days','priceFor20Days','priceForMoreDays','update','delete'];

  constructor(private hotelService: HotelService,
              private authService: AuthenticationService,
              private route: ActivatedRoute,
              public dialog: MatDialog,
              public snackBar: MatSnackBar,
              private roomService: RoomService) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.hotelService.getHotelById(id).subscribe(
        (data) => {
          this.hotel = data;
          this.userRole = this.authService.getAuthority();
          this.hotelExists = true;
          this.loadingHotel = false;
        },
        () => {
          this.loadingHotel = false;
        }
      );

      this.hotelService.getRooms(id).subscribe(
        (rooms) => {
          this.rooms = rooms;
          this.loadingRooms = false;
        },
        () => {
          this.loadingRooms = false;
        }
        );
        this.roomService.getRoomPrices().subscribe(
          (prices)=> {
          this.roomPrices = prices;
          this.loadingPrices = false;
          },
          () => {
            this.loadingPrices = false;
          }
        )
      }
  }

  pageFunction(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  openUpdateDialog() {
    const dialogRef = this.dialog.open(HotelDialogComponent,
    {
      data: this.hotel,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          this.hotelService.updateHotel(data).subscribe(
            (hotel) => {
              this.message = 'Updated successfully!';
              this.hotel = hotel;
            },
            () => {
              this.message = 'Error! Hotel is not found!';
            },
            () => {
              this.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openAddRoomDialog() {
    const dialogRef = this.dialog.open(RoomDialogComponent,
    {
      data: null,
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          let newRoom = data.room;
          newRoom.hotel = this.hotel;
          this.roomService.addRoom(newRoom).subscribe(
            (room) => {
              newRoom = room;
              this.rooms.push(room);
              data.prices.forEach(price => {
                price.room = room;
              });
              this.roomService.addRoomPrice(data.prices).subscribe(
                (prices:any) => {
                  prices.forEach(price => {
                    this.roomPrices.push(price);
                  });
                }
              );  
              this.message = 'Added room successfully!';
            },
            () => {
              this.message = 'Error! Room could not be added!';
            },
            () => {
              this.showSnackBar(this.message);
            }
          );
        }
      }
    );
  }

  openUpdateRoomDialog(room: any) {
    let roomPrices = [];
    this.roomPrices.forEach(price => {
      if(price.room.id == room.id){
        roomPrices.push(price);
      }
    });
    let parentObject = {'room': room, 'prices':roomPrices};
    const dialogRef = this.dialog.open(RoomDialogComponent,
      {
        data: parentObject,
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
      dialogRef.afterClosed().subscribe(
        (data) => {
          if (data) {
            let updRoom = data.room;
            this.roomService.updateRoom(updRoom).subscribe(
              (updatedRoom) => {
                for (let i = 0; i < this.rooms.length; i++) {
                  if (this.rooms[i].id == updRoom.id) {
                    this.rooms.splice(i, 1, updatedRoom);
                  }
                }
                this.roomService.updateRoomPrices(data.prices).subscribe(
                  (updatedRoomPrice) => {
                  }
                )
                this.message = 'Updated room successfully!';
              },
              () => {
                this.message = 'Error! Room could not be updated!';
              },
              () => {
                this.showSnackBar(this.message);
              }
            );
          }
        }
      );
  }

  deleteRoom(id: number) {
    this.roomService.deleteRoom(id).subscribe(
      () => {
        for (const i in this.rooms) {
          if (this.rooms[i].id == id) {
            this.rooms.splice(i, 1);
            break;
          }
        }
        this.message = 'Deleted room successfully!';
      },
      () => {
        this.message = 'Error! Room could not be deleted!';
      },
      () => {
        this.showSnackBar(this.message);
      }
    );
  }

  showSnackBar(message: string) {
    if (!message) {
      return;
    }
    const snackBarRef = this.snackBar.open(message, 'Dissmiss', { duration: 3000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }
}
