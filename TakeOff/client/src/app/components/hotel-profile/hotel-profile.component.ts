import { Component, OnInit } from '@angular/core';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatSnackBar, PageEvent } from '@angular/material';
import { RoomService } from 'src/app/services/room/room.service';
import { HotelDialogComponent } from '../hotel-dialog/hotel-dialog.component';
import { RoomDialogComponent } from '../room-dialog/room-dialog.component';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';
import { FormGroup, FormBuilder } from '@angular/forms';

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
  hotel: any = null;
  rooms: any = null;
  prices: any = null;
  roomPrices: any = null;
  message: string;
  userRole: string = null;
  pageSize = 10;
  pageIndex = 0;
  pageOptions = [5, 10, 25, 50];
  rating: number;
  roomRatings: any;
  url: SafeResourceUrl;
  clicked: boolean = false;
  displayedColumns: string[] = ['position', 'type', 'numberOfBeds', 'floor', 'discount', 'hasBalcony', 'hasAirCondition',
    'numberOfRooms', 'rating', 'update', 'delete'];
  displayedColumns2: string[] = ['position', 'priceFor5Days', 'priceFor10Days', 'priceFor20Days', 'priceForMoreDays'];
  displayedColumns3: string[] = ['service', 'flag', 'price'];
  displayedColumns4: string[] = ['service', 'price'];
  defaultServices: any = [];
  selectedServices: any = [];
  edit: boolean = false;
  additionalServices: FormGroup;
  transfer: any = {'cb':false,'price':''};
  parking: any = {'cb':false,'price':''};
  swimmingPool: any = {'cb':false,'price':''}; 
  breakfast: any = {'cb':false,'price':''};
  lunch: any = {'cb':false,'price':''};
  dinner: any = {'cb':false,'price':''};
  roomservice: any = {'cb':false,'price':''};
  welness: any = {'cb':false,'price':''};
  spaCenter: any = {'cb':false,'price':''};
  wifi: any = {'cb':false,'price':''};
  gym: any = {'cb':false,'price':''};
  sportsFields: any = {'cb':false,'price':''};


  constructor(private hotelService: HotelService,
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    public dialog: MatDialog,
    public snackBar: MatSnackBar,
    private roomService: RoomService,
    private formBuilder: FormBuilder,
    private sanitizer: DomSanitizer) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (!isNaN(id)) {
      this.hotelService.getHotelById(id).subscribe(
        (data) => {
          this.hotel = data;
          this.userRole = this.authService.getAuthority();
          this.hotelExists = true;
          this.loadingHotel = false;
          this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
            'https://maps.google.com/maps?q=' +
            this.hotel.location.latitude + ', ' +
            this.hotel.location.longitude +
            '&t=&z=11&ie=UTF8&iwloc=&output=embed');
          this.hotelService.getHotelRating(id).subscribe(
            (rating: number) => {
              this.rating = rating;
            }
          );
          this.hotelService.getHotelServices(id).subscribe(
            (services) => {
              this.selectedServices = services;
              this.loadingHotel = false;
              if(this.selectedServices.length != 0){
                this.selectedServices.forEach(service => {
                  //1
                  if(service.name == 'Transfer to/from the airport'){
                    this.transfer = {'cb':true,'price':service.price};
                  }
                  //2
                  if(service.name == 'Parking'){
                    this.parking = {'cb':true,'price':service.price};
                  }
                  //3
                  if(service.name == 'Swimming pool'){
                    this.swimmingPool = {'cb':true,'price':service.price};
                  }
                  //4
                  if(service.name == 'Breakfast'){
                    this.breakfast = {'cb':true,'price':service.price};
                  }
                  //5
                  if(service.name == 'Lunch'){
                    this.lunch = {'cb':true,'price':service.price};
                  }
                  //6
                  if(service.name == 'Dinner'){
                    this.dinner = {'cb':true,'price':service.price};
                  }
                  //7
                  if(service.name == 'Room service'){
                    this.roomservice = {'cb':true,'price':service.price};
                  }
                  //8
                  if(service.name == 'Welness'){
                    this.welness = {'cb':true,'price':service.price};
                  }
                  //9
                  if(service.name == 'Spa center'){
                    this.spaCenter = {'cb':true,'price':service.price};
                  }
                  //10
                  if(service.name == 'WiFi'){
                    this.wifi = {'cb':true,'price':service.price};
                  }
                  //11
                  if(service.name == 'Gym'){
                    this.gym = {'cb':true,'price':service.price};
                  }
                  //12
                  if(service.name == 'Sports fields'){
                    this.sportsFields = {'cb':true,'price':service.price};
                  }
                });
                this.additionalServices = this.formBuilder.group({
                  CB1: [this.transfer.cb],
                  CB2: [this.parking.cb],
                  CB3: [this.swimmingPool.cb],
                  CB4: [this.breakfast.cb],
                  CB5: [this.lunch.cb],
                  CB6: [this.dinner.cb],
                  CB7: [this.roomservice.cb],
                  CB8: [this.welness.cb],
                  CB9: [this.spaCenter.cb],
                  CB10: [this.wifi.cb],
                  CB11: [this.gym.cb],
                  CB12: [this.sportsFields.cb],
                  Price1: [this.transfer.price],
                  Price2: [this.parking.price],
                  Price3: [this.swimmingPool.price],
                  Price4: [this.breakfast.price],
                  Price5: [this.lunch.price],
                  Price6: [this.dinner.price],
                  Price7: [this.roomservice.price],
                  Price8: [this.welness.price],
                  Price9: [this.spaCenter.price],
                  Price10: [this.wifi.price],
                  Price11: [this.gym.price],
                  Price12: [this.sportsFields.price]
                });
            }else{
              this.additionalServices = this.formBuilder.group({
                CB1: [],
                CB2: [],
                CB3: [],
                CB4: [],
                CB5: [],
                CB6: [],
                CB7: [],
                CB8: [],
                CB9: [],
                CB10: [],
                CB11: [],
                CB12: [],
                Price1: [],
                Price2: [],
                Price3: [],
                Price4: [],
                Price5: [],
                Price6: [],
                Price7: [],
                Price8: [],
                Price9: [],
                Price10: [],
                Price11: [],
                Price12: []
              });
            }
            }
          );
        },
        () => {
          this.loadingHotel = false;
        }
      );

      this.hotelService.getRooms(id).subscribe(
        (rooms) => {
          this.rooms = rooms;
          this.hotelService.getRoomRatings(id).subscribe(
            (ratings) => {
              this.roomRatings = ratings;
              this.loadingRooms = false;
            }
          );
        },
        () => {
          this.loadingRooms = false;
        }
      );

      this.roomService.getRoomPrices().subscribe(
        (prices) => {
          this.roomPrices = prices;
          this.loadingPrices = false;
        },
        () => {
          this.loadingPrices = false;
        }
      );

      this.hotelService.getServices().subscribe(
        (services) => {
          this.defaultServices = services;
        }
      );
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
              let rating = { 'rating': 0.0, 'room': room };
              this.roomRatings.push(rating);
              data.prices.forEach(price => {
                price.room = room;
              });
              this.roomService.addRoomPrice(data.prices).subscribe(
                (prices: any) => {
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
      if (price.room.id == room.id) {
        roomPrices.push(price);
      }
    });
    let parentObject = { 'room': room, 'prices': roomPrices };
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
          console.log(data);
          let updRoom = data.room;
          this.roomService.updateRoom(updRoom).subscribe(
            (updatedRoom) => {
              console.log(updatedRoom);
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

  save() {
    this.selectedServices = [];
    if (this.additionalServices.get('CB1').value) {
      let price = this.additionalServices.get('Price1').value;
      let name = this.defaultServices[0].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB2').value) {
      let price = this.additionalServices.get('Price2').value;
      let name = this.defaultServices[1].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB3').value) {
      let price = this.additionalServices.get('Price3').value;
      let name = this.defaultServices[2].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB4').value) {
      let price = this.additionalServices.get('Price4').value;
      let name = this.defaultServices[3].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB5').value) {
      let price = this.additionalServices.get('Price5').value;
      let name = this.defaultServices[4].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB6').value) {
      let price = this.additionalServices.get('Price6').value;
      let name = this.defaultServices[5].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB7').value) {
      let price = this.additionalServices.get('Price7').value;
      let name = this.defaultServices[6].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB8').value) {
      let price = this.additionalServices.get('Price8').value;
      let name = this.defaultServices[7].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB9').value) {
      let price = this.additionalServices.get('Price9').value;
      let name = this.defaultServices[8].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB10').value) {
      let price = this.additionalServices.get('Price10').value;
      let name = this.defaultServices[9].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB11').value) {
      let price = this.additionalServices.get('Price11').value;
      let name = this.defaultServices[10].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }
    if (this.additionalServices.get('CB12').value) {
      let price = this.additionalServices.get('Price12').value;
      let name = this.defaultServices[11].name;
      this.selectedServices.push({ 'name': name, 'price': price, 'hotel': this.hotel });
    }

    this.hotelService.addHotelServices(this.selectedServices).subscribe(
      (services) => {
        this.edit = false;
      }
    );
  }
}
