import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';

declare let require: any;

@Component({
  selector: 'app-rent-a-car-dialog',
  templateUrl: './rent-a-car-dialog.component.html',
  styleUrls: ['./rent-a-car-dialog.component.css']
})
export class RentACarDialogComponent implements OnInit {

  rentACarForm: FormGroup;
  update = true;
  location: any = {};

  constructor(private dialogRef: MatDialogRef<RentACarDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public rentACar: any) { }

    ngOnInit(): void {
      if (!this.rentACar) {
        this.rentACar = {'name': '', 'location': {'address' : ''}, 'description': ''};
        this.update = false;
      } else {
        this.location = JSON.parse(JSON.stringify(this.rentACar.location));
      }

      this.rentACarForm = this.formBuilder.group({
         name: [this.rentACar.name, Validators.required],
         address: [this.rentACar.location.address, Validators.required],
         description: [this.rentACar.description]
      });

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
        console.log(this.location);
      });

      placesAutocomplete.on('clear', e => {
        this.rentACarForm.controls.address.setValue('');
      });
    }

    submitForm() {
      if (this.rentACarForm.valid) {
        const rentACar = this.rentACarForm.value;
        delete rentACar['address'];
        rentACar.location = this.location;
        rentACar.id = this.rentACar.id;
        this.dialogRef.close(rentACar);
      }
    }
}
