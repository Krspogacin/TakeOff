import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-room-dialog',
  templateUrl: './room-dialog.component.html',
  styleUrls: ['./room-dialog.component.css']
})
export class RoomDialogComponent implements OnInit {

  roomForm: FormGroup;
  roomPriceForm: FormGroup;
  update = false;
  numberOfRoomsMin = 1;
  period5: any = null;
  period10: any = null;
  period20: any = null;
  period20i: any = null;
  periods = ['5','10','20','More than 20'];
  superObject = {'room':{},'prices':[]};

  constructor(private dialogRef: MatDialogRef<RoomDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public parentObject: any) { }

  ngOnInit() {
    if(!this.parentObject){
      this.parentObject = {'room':null,'prices':null};
      this.parentObject.room = {'numberOfBeds':'','type': '', 'floor':'', 'defaultPrice':'','discount':'',
                   'hasAirCondition':'','hasBalcony':'', 'numberOfRooms':'','isReserved':false}
    }else{
      this.update = true;
      this.parentObject.prices.forEach(price => {
        if (price.period == '5'){
          this.period5 = price.price;
        }else if (price.period == '10'){
          this.period10 = price.price;
        }else if (price.period == '20'){
          this.period20 = price.price;
        }else{
          this.period20i = price.price;
        }
      });
    }
    this.roomForm = this.formBuilder.group({
      numberOfBeds: [this.parentObject.room.numberOfBeds, [Validators.required,Validators.min(1)]],
      type: [this.parentObject.room.type, Validators.required],
      floor: [this.parentObject.room.floor, [Validators.required,Validators.min(1)]],
      defaultPrice: [this.parentObject.room.defaultPrice, [Validators.required,Validators.min(1)]],
      discount: [this.parentObject.room.discount],
      hasAirCondition: [this.parentObject.room.hasAirCondition],
      hasBalcony: [this.parentObject.room.hasBalcony],
      numberOfRooms: [this.parentObject.room.numberOfRooms, [Validators.required,Validators.min(1)]]
    })

    this.roomPriceForm = this.formBuilder.group({
      Period5: [this.period5],
      Period10: [this.period10],
      Period20: [this.period20],
      MoreThan: [this.period20i]
    })
  }

  submit(){
    const newRoom = this.roomForm.value;
    newRoom.id = this.parentObject.room.id;
    newRoom.reserved = this.parentObject.room.reserved;
    this.superObject.room = newRoom;
    if(this.parentObject.prices){
      this.parentObject.prices.forEach(price => {
        if(price.period == '5'){
          if(this.roomPriceForm.value.Period5 != null){
            price.price = this.roomPriceForm.value.Period5;
          }else{
            price.price = newRoom.defaultPrice;
          }
        }else if(price.period == '10'){
          if(this.roomPriceForm.value.Period10 != null){
            price.price = this.roomPriceForm.value.Period10;
          }else{
            price.price = newRoom.defaultPrice;
          }
        }else if(price.period == '20'){
          if(this.roomPriceForm.value.Period20 != null){
            price.price = this.roomPriceForm.value.Period20;
          }else{
            price.price = newRoom.defaultPrice;
          }
        }else{
          if(this.roomPriceForm.value.MoreThan != null){
            price.price = this.roomPriceForm.value.MoreThan;
          }else{
          price.price = newRoom.defaultPrice;
          }
        }
      });
    }else{
      this.parentObject.prices = [];
      let price5 = {'period':'','price':0};
      let price10 = {'period':'','price':0};
      let price20 = {'period':'','price':0};
      let price20i = {'period':'','price':0};
      if(this.roomPriceForm.value.Period5 != null){
        price5.period = '5';
        price5.price = this.roomPriceForm.value.Period5;
        this.parentObject.prices.push(price5);
      }else{
        price5.period = '5';
        price5.price = newRoom.defaultPrice;
        this.parentObject.prices.push(price5);
      }
      if(this.roomPriceForm.value.Period10 != null){
        price10.period = '10';
        price10.price = this.roomPriceForm.value.Period10;
        this.parentObject.prices.push(price10);
      }else{
        price10.period = '10';
        price10.price = newRoom.defaultPrice;
        this.parentObject.prices.push(price10);
      }
      if(this.roomPriceForm.value.Period20 != null){
        price20.period = '20';
        price20.price = this.roomPriceForm.value.Period20;
        this.parentObject.prices.push(price20);
      }else{
        price20.period = '20';
        price20.price = newRoom.defaultPrice;
        this.parentObject.prices.push(price20);
      }
      if(this.roomPriceForm.value.MoreThan != null){
        price20i.period = 'MoreThan20';
        price20i.price = this.roomPriceForm.value.MoreThan;
        this.parentObject.prices.push(price20i);
      }else{
        price20i.period = 'MoreThan20';
        price20i.price = newRoom.defaultPrice;
        this.parentObject.prices.push(price20i);
      }
    }
    this.superObject.prices = this.parentObject.prices;
    this.dialogRef.close(this.superObject);
  }

}
