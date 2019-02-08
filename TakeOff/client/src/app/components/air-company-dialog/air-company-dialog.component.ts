import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-air-company-dialog',
  templateUrl: './air-company-dialog.component.html',
  styleUrls: ['./air-company-dialog.component.css']
})
export class AirCompanyDialogComponent implements OnInit {

  companyUpdateForm: FormGroup;
  update = false;
  company = {
    'id': null,
    'name': '',
    'location': { 'address': '' },
    'description': '',
    'version': null
  };
  location: any = {};
  destinations = [];

  // @ViewChild('destInput') destInput: ElementRef<HTMLInputElement>;
  // @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(private dialogRef: MatDialogRef<AirCompanyDialogComponent>,
    private formBuilder: FormBuilder, @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {
    if (this.data) {
      this.update = true;
      this.company = this.data.company;
      this.location = this.company.location;
      this.destinations = JSON.parse(JSON.stringify(this.data.destinations));
    }

    this.companyUpdateForm = this.formBuilder.group({
      name: [this.company.name, Validators.required],
      address: [this.company.location.address, Validators.required],
      description: [this.company.description],
      destinations: []
    });

    // address field
    const placesAddress = require('places.js');
    const placesAddressAutocomplete = placesAddress({
      appId: 'pl14EZX3IQNN',
      apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
      container: document.querySelector('#address-input')
    });

    placesAddressAutocomplete.on('change', e => {
      this.location.id = null;
      this.location.address = e.suggestion.value;
      this.location.country = e.suggestion.country;
      this.location.city = e.suggestion.city ? e.suggestion.city : e.suggestion.name;
      this.location.latitude = e.suggestion.latlng.lat;
      this.location.longitude = e.suggestion.latlng.lng;
    });

    placesAddressAutocomplete.on('clear', e => {
      this.companyUpdateForm.controls.address.setValue('');
    });

    // destinations field
    const placesDestinations = require('places.js');
    const placesDestinationsAutocomplete = placesDestinations({
      appId: 'pl14EZX3IQNN',
      apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
      container: document.querySelector('#dest-input')
    });

    placesDestinationsAutocomplete.on('change', e => {
      const location: any = {};
      location.id = null;
      location.address = e.suggestion.value;
      location.country = e.suggestion.country;
      location.city = e.suggestion.city ? e.suggestion.city : e.suggestion.name;
      location.latitude = e.suggestion.latlng.lat;
      location.longitude = e.suggestion.latlng.lng;

      let exists = false;
      this.destinations.forEach(element => {
        if (element.latitude === location.latitude && element.longitude === location.longitude) {
          exists = true;
        }
      });

      if (!exists) {
        this.destinations.push(location);
      }
      this.companyUpdateForm.controls.destinations.setValue('');
    });

  }

  // filterDestinations(value: string): Destination[] {
  //   const filterValue = value.toLowerCase();
  //   return this.allDestinations.filter(dest => dest.city.toLowerCase().indexOf(filterValue) === 0 ||
  //     dest.country.toLowerCase().indexOf(filterValue) === 0);
  // }

  removeDestination(destination): void {
    const index = this.destinations.indexOf(destination);

    if (index >= 0) {
      this.destinations.splice(index, 1);
    }
  }

  // selectDestination(event: MatAutocompleteSelectedEvent): void {
  //   const destination = event.option.value;
  //   this.destInput.nativeElement.value = '';
  //   this.companyUpdateForm.controls.destinations.setValue('');

  //   let index = -1;
  //   for (let i = 0; i < this.destinations.length; i++) {
  //     if (destination.id === this.destinations[i].id) {
  //       index = i;
  //       break;
  //     }
  //   }

  //   if (index < 0) {
  //     this.destinations.push(destination);
  //   }
  // }

  submitForm() {
    const company = this.companyUpdateForm.value;
    delete company.address;
    delete company.destinations;
    company.id = this.company.id;
    company.location = this.location;
    company.version = this.company.version;

    const updated = {
      'company': company,
      'destinations': this.destinations
    };

    this.dialogRef.close(updated);
  }

}
