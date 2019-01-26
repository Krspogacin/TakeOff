import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatAutocomplete, MatAutocompleteSelectedEvent, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

export interface Destination {
  id: number;
  airportName: string;
  city: string;
  country: string;
  version: number;
}

@Component({
  selector: 'app-air-company-dialog',
  templateUrl: './air-company-dialog.component.html',
  styleUrls: ['./air-company-dialog.component.css']
})
export class AirCompanyDialogComponent implements OnInit {

  companyUpdateForm: FormGroup;
  destinations = [];
  allDestinations: Destination[] = [];
  filteredDestinations: Observable<Destination[]>;

  @ViewChild('destInput') destInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(private airCompanyService: AirCompanyService, private dialogRef: MatDialogRef<AirCompanyDialogComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {
    this.destinations = JSON.parse(JSON.stringify(this.data.destinations));

    this.airCompanyService.getAllDestinations().subscribe(
      (data: []) => {
        this.allDestinations = data;
      });

    this.companyUpdateForm = this.formBuilder.group({
      name: [this.data.company.name, Validators.required],
      address: [this.data.company.address, Validators.required],
      description: [this.data.company.description],
      destinations: []
    });

    this.filteredDestinations = this.companyUpdateForm.controls.destinations.valueChanges
      .pipe(
        startWith(''),
        // map(value => typeof value === 'string' ? value : value.city),
        map(value => typeof value === 'string' ? this.filterDestinations(value) : this.allDestinations.slice())
      );
  }

  filterDestinations(value: string): Destination[] {
    const filterValue = value.toLowerCase();
    return this.allDestinations.filter(dest => dest.city.toLowerCase().includes(filterValue) ||
      dest.country.toLowerCase().includes(filterValue));
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
    this.companyUpdateForm.controls.destinations.setValue('');

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
    this.companyUpdateForm.value.id = this.data.company.id;
    this.companyUpdateForm.value.version = this.data.company.version;
    delete this.companyUpdateForm.value.destinations;
    const updated = {
      'company': this.companyUpdateForm.value,
      'destinations': this.destinations
    };

    this.dialogRef.close(updated);
  }

}
