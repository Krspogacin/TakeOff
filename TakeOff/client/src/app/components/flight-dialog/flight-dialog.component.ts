import { Component, OnInit, Inject, ElementRef, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

export interface Destination {
  id: number;
  airportName: string;
  city: string;
  country: string;
  version: number;
}

@Component({
  selector: 'app-flight-dialog',
  templateUrl: './flight-dialog.component.html',
  styleUrls: ['./flight-dialog.component.css']
})
export class FlightDialogComponent implements OnInit {

  flight = {
    'id': null,
    'takeOffDate': '',
    'landingDate': '',
    'distance': null,
    'ticketPrice': null,
    'version': null
  };
  flightForm: FormGroup;
  update = false;
  destinations = [];
  allDestinations: Destination[] = [];
  filteredDestinations: Observable<Destination[]>;

  @ViewChild('destInput') destInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(private airCompanyService: AirCompanyService, private dialogRef: MatDialogRef<FlightDialogComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {

    this.airCompanyService.getAllDestinations().subscribe(
      (data: []) => {
        this.allDestinations = data;
      });

    if (this.data) {
      this.flight = this.data.flight;
      this.destinations = JSON.parse(JSON.stringify(this.data.destinations));
      this.update = true;
    }

    this.flightForm = this.formBuilder.group({
      takeOffDate: [this.flight.takeOffDate, Validators.required],
      landingDate: [this.flight.landingDate, Validators.required],
      distance: [this.flight.distance, Validators.required],
      ticketPrice: [this.flight.ticketPrice, Validators.required],
      destinations: []
    });

    this.filteredDestinations = this.flightForm.controls.destinations.valueChanges
      .pipe(
        startWith(''),
        // map(value => typeof value === 'string' ? value : value.city),
        map(value => typeof value === 'string' ? this.filterDestinations(value) : this.allDestinations.slice())
      );

  }

  filterDestinations(value: string): Destination[] {
    const filterValue = value.toLowerCase();
    return this.allDestinations.filter(dest => dest.city.toLowerCase().indexOf(filterValue) === 0 ||
      dest.country.toLowerCase().indexOf(filterValue) === 0);
  }

  removeDestination(destination: Destination): void {
    const index = this.destinations.indexOf(destination);

    if (index >= 0) {
      this.destinations.splice(index, 1);
    }
  }

  selectDestination(event: MatAutocompleteSelectedEvent): void {
    const destination = event.option.value;
    this.destInput.nativeElement.value = '';
    this.flightForm.controls.destinations.setValue('');

    let index = -1;
    for (let i = 0; i < this.destinations.length; i++) {
      if (destination.id === this.destinations[i].id) {
        index = i;
        break;
      }
    }

    if (index < 0) {
      this.destinations.push(destination);
    }
  }


  submitForm() {
    this.flightForm.value.id = this.flight.id;
    this.flightForm.value.version = this.flight.version;
    delete this.flightForm.value.destinations;
    const updated = {
      'flight': this.flightForm.value,
      'destinations': this.destinations
    };

    this.dialogRef.close(updated);
  }

}
