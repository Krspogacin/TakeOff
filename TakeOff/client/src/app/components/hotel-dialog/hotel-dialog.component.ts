import { Component, OnInit, Inject, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

declare let require: any;

@Component({
  selector: 'app-hotel-dialog',
  templateUrl: './hotel-dialog.component.html',
  styleUrls: ['./hotel-dialog.component.css']
})
export class HotelDialogComponent implements OnInit,AfterViewInit {

  hotelUpdateForm: FormGroup;
  location: any;


  constructor(private dialogRef: MatDialogRef<HotelDialogComponent>,
              private formBuilder: FormBuilder,
             @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.hotelUpdateForm = this.formBuilder.group({
      name: [this.data.name,Validators.required],
      address: [this.data.location.address,Validators.required],
      description: [this.data.description]
    });
  }

  ngAfterViewInit(): void {
    const places = require('places.js');
      const placesAutocomplete = places({
        appId: 'pl14EZX3IQNN',
        apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
        container: document.querySelector('#address-input')
      });

      placesAutocomplete.on('change', e => {
        this.location.id = null;
        this.location.address = e.suggestion.value;
        this.location.country = e.suggestion.country;
        this.location.city = e.suggestion.city ? e.suggestion.city : e.suggestion.name;
        this.location.latitude = e.suggestion.latlng.lat;
        this.location.longitude = e.suggestion.latlng.lng;
      });

      placesAutocomplete.on('clear', e => {
        this.hotelUpdateForm.controls.address.setValue('');
      });
    }

  submitForm() {
    const updatedHotel = this.hotelUpdateForm.value;
    updatedHotel.id = this.data.id;
    delete updatedHotel['address'];
    console.log(this.location);
    if(this.location){
      updatedHotel.location = this.location;
    }else{
      updatedHotel.location = this.data.location;
    }
    this.dialogRef.close(updatedHotel);
  }

}
