import { Component, OnInit, Inject } from '@angular/core';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { HotelService } from 'src/app/services/hotel/hotel.service';

@Component({
  selector: 'app-hotel-reserve-dialog',
  templateUrl: './hotel-reserve-dialog.component.html',
  styleUrls: ['./hotel-reserve-dialog.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class HotelReserveDialogComponent implements OnInit {

  selectionForm: FormGroup;
  parametersForm: FormGroup;
  CBForm: FormGroup;
  nightsNumberMin = 1;
  rooms = 0;
  availableRooms: any = [];
  beds = [];
  numberOfRooms = 0;
  selectedRooms = 0;
  complexObject:any = {};
  additionalServices: any = [];
  displayedColumns: string[] = ['name', 'price', 'select'];

  constructor(private dialogRef: MatDialogRef<HotelReserveDialogComponent>,
              private formBuilder: FormBuilder,
              private hotelService: HotelService,
              @Inject(MAT_DIALOG_DATA) public hotel: any) { }

  ngOnInit() {
    this.parametersForm = this.formBuilder.group({
      checkInDate: ['',Validators.required],
      nightsNumber: ['',[Validators.required,Validators.min(1)]],
      guestsNumber: ['',[Validators.required,Validators.min(1)]],
      roomsNumber: ['',[Validators.required,Validators.min(1)]],
      minPrice: [],
      maxPrice: []
    });

    this.selectionForm = new FormGroup({});
    this.CBForm = new FormGroup({});
    this.hotelService.getHotelServices(this.hotel).subscribe(
      (services) => {
        this.additionalServices = services;
        this.additionalServices.forEach(elem => {
          this.CBForm.addControl('CB' + elem.id,this.formBuilder.control(false));
        });
    });
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

  selectRoom(value: number){
    this.selectedRooms = value; 
  }

  getRooms(){
    if(!this.parametersForm.invalid){
      this.complexObject.hotelId = this.hotel;
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
      console.log(this.complexObject);
      this.hotelService.getAvailableRooms(this.complexObject).subscribe(
        (rooms) => {
          console.log(rooms);
          this.availableRooms = rooms;
          this.availableRooms.forEach(room => {
            this.selectionForm.addControl('Room' + room.room.id,this.formBuilder.control('',[Validators.min(1),Validators.max(this.numberOfRooms)]));
          });
        }
      );
    }
  }

}
