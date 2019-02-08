import { Component, OnInit, Inject, AfterViewInit } from '@angular/core';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';

@Component({
  selector: 'app-hotel-reserve-dialog',
  templateUrl: './hotel-reserve-dialog.component.html',
  styleUrls: ['./hotel-reserve-dialog.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class HotelReserveDialogComponent implements OnInit,AfterViewInit {

  selectionForm: FormGroup;
  parametersForm: FormGroup;
  CBForm: FormGroup;
  nightsNumberMin = 1;
  rooms = 0;
  availableRooms: any = [];
  beds = [];
  numberOfRooms: number = 0;
  selectedRooms = 0;
  complexObject:any = {};
  additionalServices: any = [];
  displayedColumns: string[] = ['name', 'price', 'select'];
  totalPricee = 0;
  reservationObject: any = {};
  error = false;
  landingDate: any;
  roomLoaded: boolean = false;

  constructor(private dialogRef: MatDialogRef<HotelReserveDialogComponent>,
              private formBuilder: FormBuilder,
              private hotelService: HotelService,
              private reservationService: ReservationService,
              @Inject(MAT_DIALOG_DATA) public object : any) { }

  ngOnInit() {
    if (!this.object.hotel || !this.object.landingDate || !(this.object.landingDate instanceof Date)) {
      this.error = true;
      return;
    }
    this.landingDate = this.object.landingDate;
    const id = this.object.reservation;
    this.parametersForm = this.formBuilder.group({
      checkInDate: ['',Validators.required],
      nightsNumber: ['',[Validators.required,Validators.min(1)]],
      guestsNumber: ['',[Validators.required,Validators.min(1)]],
      roomsNumber: ['',[Validators.required,Validators.min(1)]],
      minPrice: [],
      maxPrice: []
    });
    this.reservationService.getNumberOfUsers(id).subscribe(
      (data : number) => {
        this.parametersForm.get('guestsNumber').setValidators(Validators.max(data));
      }
    );

    this.selectionForm = new FormGroup({});
    this.CBForm = new FormGroup({});
    this.hotelService.getHotelServices(this.object.hotel.id).subscribe(
      (services) => {
        this.additionalServices = services;
        this.additionalServices.forEach(elem => {
          this.CBForm.addControl('CB' + elem.id,this.formBuilder.control(false));
        });
    });
  }

  ngAfterViewInit(){
    
  }

  change(value: number){
    this.beds.forEach(bed => {
      this.parametersForm.removeControl('Bed' + bed);
    });
    this.beds = [];
    if(value != null){
          this.rooms = value;
          for(let i = 0; i < this.rooms; i++){
            this.beds[i] = i;
            this.parametersForm.addControl('Bed' + i,this.formBuilder.control('',[Validators.required,Validators.min(1)]));
          }
    }else{
      this.rooms = 0;
    } 
  }

  selectRoom(id:any,value: any){
    this.selectedRooms = 0;
    this.availableRooms.forEach(room => {
      if(room.room.id == id){
        if(value != '' && value){
          room.no = value;
        }else{
          room.no = 0;
        }
      }
      this.selectedRooms = this.selectedRooms + parseInt(room.no,10);
    });
    if((this.numberOfRooms-this.selectedRooms) < 0){
      this.availableRooms.forEach(room => {
        this.selectionForm.get('Room'+room.room.id).setErrors({ sum: true })
      });
    }else{
      this.availableRooms.forEach(room => {
        if(value > room.number){
          this.selectionForm.get('Room'+room.room.id).setErrors({ NoOfRooms:true });
        }else{
          this.selectionForm.get('Room'+room.room.id).setErrors(null);
        }
        if(this.numberOfRooms != this.selectedRooms){
          this.selectionForm.get('Room'+room.room.id).setErrors({ NeedToBeEqual:true })
        }else{
          this.selectionForm.get('Room'+room.room.id).setErrors(null);
        }
      });
    }
  }

  validation(){
    let sum = 0;
    this.beds.forEach(bed => {
      sum = sum + this.parametersForm.get('Bed' + bed).value;
    });
    if(sum != this.parametersForm.get('guestsNumber').value){
      this.parametersForm.get('guestsNumber').setErrors({ sameNumber: true})
      this.beds.forEach(bed => {
        this.parametersForm.get('Bed' + bed).setErrors({ sameNumber: true });
      });
    }else{
      if(this.parametersForm.get('guestsNumber').value && this.parametersForm.get('guestsNumber').value != ''){
        this.parametersForm.get('guestsNumber').setErrors(null);
      }
      this.beds.forEach(bed => {
        if(this.parametersForm.get('Bed' + bed).value &&
          this.parametersForm.get('Bed' + bed).value != '' &&
          this.parametersForm.get('Bed' + bed).value != 0){
            this.parametersForm.get('Bed' + bed).setErrors(null);
        }
      });
    }
  }

  getRooms(){
    if(!this.parametersForm.invalid){
      this.complexObject.hotelId = this.object.hotel.id;
      this.complexObject.checkIn = this.parametersForm.get('checkInDate').value;
      this.complexObject.numberOfDays = this.parametersForm.get('nightsNumber').value;
      this.complexObject.numberOfRooms = this.parametersForm.get('roomsNumber').value;
      this.numberOfRooms = this.parametersForm.get('roomsNumber').value;
      this.complexObject.numberOfBeds = [];
      this.beds.forEach(bed => {
        if(this.parametersForm.get('Bed' + bed).value != '' && 
        this.parametersForm.get('Bed' + bed).value != null){
          this.complexObject.numberOfBeds.push(this.parametersForm.get('Bed' + bed).value);
        }
      });
      this.complexObject.priceMin = this.parametersForm.get('minPrice').value;
      this.complexObject.priceMax = this.parametersForm.get('maxPrice').value;
      this.hotelService.getAvailableRooms(this.complexObject).subscribe(
        (rooms) => {
          this.availableRooms = rooms;
          this.availableRooms.forEach(room => {
            room.no = 0;
            this.selectionForm.addControl('Room' + room.room.id,this.formBuilder.control('',[Validators.min(1),Validators.max(this.numberOfRooms)]));
          });
          this.roomLoaded = true;
        }
      );
    }
  }

  tp(){
    this.totalPricee = 0;
    this.availableRooms.forEach(room => {
      if(this.selectionForm.get('Room'+room.room.id).value && this.selectionForm.get('Room'+room.room.id).value != ''){
        this.totalPricee = this.totalPricee + this.selectionForm.get('Room'+room.room.id).value*room.totalPrice;
      }
    });
  }

  totalPrice(id: any){
    this.additionalServices.forEach(service => {
      if(service.id == id && this.CBForm.get('CB'+service.id).value == true){
        this.totalPricee = this.totalPricee + service.price * this.parametersForm.get('guestsNumber').value;
      }else if(service.id == id && this.CBForm.get('CB'+service.id).value == false){
        this.totalPricee = this.totalPricee - service.price * this.parametersForm.get('guestsNumber').value;
      }
    });
  }

  reservation(){
    const reservationEndDate = new Date(this.availableRooms[0].endingDate);
    this.reservationObject.reservationEndDate = new Date(Date.UTC(reservationEndDate.getFullYear(),
                                                                  reservationEndDate.getMonth(),
                                                                  reservationEndDate.getDate()));
    const reservationStartDate = this.parametersForm.get('checkInDate').value;
    this.reservationObject.reservationStartDate = new Date(Date.UTC(reservationStartDate.getFullYear(),
                                                                    reservationStartDate.getMonth(),
                                                                    reservationStartDate.getDate()));
    this.reservationObject.totalPrice = this.totalPricee;
    this.reservationObject.roomsAndRatings = [];
    this.availableRooms.forEach(room => {
      const number = parseInt(room.no,10);
      if (number != 0){
        for(let i = 0; i < number; i++){
          this.reservationObject.roomsAndRatings.push({'room': room.room});
        }
      }
    });
    console.log(this.reservationObject);
    this.dialogRef.close(this.reservationObject);
  }

}
