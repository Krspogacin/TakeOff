import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

declare let require: any;

@Component({
  selector: 'app-office-dialog',
  templateUrl: './office-dialog.component.html',
  styleUrls: ['./office-dialog.component.css']
})
export class OfficeDialogComponent implements OnInit {

  officeForm: FormGroup;
  update = true;
  location: any = {};

  constructor(private dialogRef: MatDialogRef<OfficeDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public office: any) { }

    ngOnInit(): void {
      if (!this.office) {
        this.office = {'name': '', 'location': {'address' : ''}};
        this.update = false;
      } else {
        this.location = JSON.parse(JSON.stringify(this.office.location));
      }

      this.officeForm = this.formBuilder.group({
         name: [this.office.name, Validators.required],
         address: [this.office.location.address, Validators.required]
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
      });

      placesAutocomplete.on('clear', e => {
        this.officeForm.controls.address.setValue('');
      });
    }

    submitForm() {
      if (this.officeForm.valid) {
        const office = this.officeForm.value;
        delete office['address'];
        office.location = this.location;
        office.id = this.office.id;
        this.dialogRef.close(office);
      }
    }
}
