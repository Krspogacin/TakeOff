import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-flight-dialog',
  templateUrl: './flight-dialog.component.html',
  styleUrls: ['./flight-dialog.component.css']
})
export class FlightDialogComponent implements OnInit {

  flight = {
    'id': null,
    'takeOffLocation': { 'address': '' },
    'landingLocation': { 'address': '' },
    'takeOffDate': '',
    'landingDate': '',
    'ticketPrice': null
  };
  flightForm: FormGroup;
  takeOffLocation: any = {};
  landingLocation: any = {};
  update = false;
  destinations = [];

  constructor(private dialogRef: MatDialogRef<FlightDialogComponent>, private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {

    if (this.data) {
      this.flight = this.data.flight;
      this.takeOffLocation = this.data.flight.takeOffLocation;
      this.landingLocation = this.data.flight.landingLocation;
      this.destinations = JSON.parse(JSON.stringify(this.data.destinations));
      this.update = true;
    }

    this.flightForm = this.formBuilder.group({
      takeOffLocation: [this.flight.takeOffLocation.address, Validators.required],
      landingLocation: [this.flight.landingLocation.address, Validators.required],
      takeOffDate: [this.flight.takeOffDate, Validators.required],
      landingDate: [this.flight.landingDate, Validators.required],
      ticketPrice: [this.flight.ticketPrice, Validators.required],
      destinations: []
    });

    // take off location field
    const placesTakeOff = require('places.js');
    const placesTakeOffAutocomplete = placesTakeOff({
      appId: 'pl14EZX3IQNN',
      apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
      container: document.querySelector('#takeOffLocation-input')
    });

    placesTakeOffAutocomplete.on('change', e => {
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
        this.takeOffLocation = location;
      }
    });

    // landing location field
    const placesLanding = require('places.js');
    const placesLandingAutocomplete = placesLanding({
      appId: 'pl14EZX3IQNN',
      apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
      container: document.querySelector('#landingLocation-input')
    });

    placesLandingAutocomplete.on('change', e => {
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
        this.landingLocation = location;
      }
    });

    // transfer locations field
    const placesTransferLocations = require('places.js');
    const placesTransferLocationsAutocomplete = placesTransferLocations({
      appId: 'pl14EZX3IQNN',
      apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
      container: document.querySelector('#transferLocations-input')
    });

    placesTransferLocationsAutocomplete.on('change', e => {
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

      // add only if it doesn't exist and is different from take off and landing location
      if (!exists && location.latitude !== this.takeOffLocation.latitude && location.longitude !== this.takeOffLocation.longitude
        && location.latitude !== this.landingLocation.latitude && location.longitude !== this.landingLocation.longitude) {
        this.destinations.push(location);
      }

      this.flightForm.controls.destinations.setValue('');
    });
  }

  removeDestination(destination) {
    const index = this.destinations.indexOf(destination);

    if (index >= 0) {
      this.destinations.splice(index, 1);
    }
  }

  submitForm() {
    const flight = this.flightForm.value;
    delete flight.destinations;
    flight.id = this.flight.id;
    flight.takeOffLocation = this.takeOffLocation;
    flight.landingLocation = this.landingLocation;

    const updated = {
      'flight': this.flightForm.value,
      'destinations': this.destinations
    };

    this.dialogRef.close(updated);
  }

}
