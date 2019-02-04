import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSelectionList, MatList, MatListOption } from '@angular/material';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { VehicleService } from 'src/app/services/vehicle/vehicle.service';
import { SelectionModel } from '@angular/cdk/collections';
import { element } from 'protractor';

@Component({
  selector: 'app-vehicle-reservation-dialog',
  templateUrl: './vehicle-reservation-dialog.component.html',
  styleUrls: ['./vehicle-reservation-dialog.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class VehicleReservationDialogComponent implements OnInit {

  reservationForm: FormGroup;
  secondFormGroup: FormGroup;
  vehicles: any[] = [];
  minPassengers = 1;
  maxPassengers = 7;
  fuelTypes: any[] = [];
  transmissionTypes: any[] = [];
  @ViewChild('fuelTypesList') fuelTypesList: MatSelectionList;
  @ViewChild('transmissionTypesList') transmissionTypesList: MatSelectionList;

  constructor(private rentACarService: RentACarService,
              private vehicleService: VehicleService,
              private authService: AuthenticationService,
              private dialogRef: MatDialogRef<VehicleReservationDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public rentACar: any) { }

  ngOnInit() {

    if (!this.rentACar || !this.authService.getUsername() || this.authService.getAuthority() !== 'ROLE_USER') {
      this.dialogRef.close();
    }

    // this.fuelTypesList.selectedOptions = new SelectionModel<MatListOption>(false);

    this.vehicleService.getFuelTypes().subscribe(
      (data: any[]) => {
        this.fuelTypes = data;
      }
    );
    this.vehicleService.getTransmissionTypes().subscribe(
      (data: any[]) => {
        this.transmissionTypes = data;
      }
    );

    this.reservationForm = this.formBuilder.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      numOfPassengers: ['', [Validators.required, Validators.min(this.minPassengers), Validators.max(this.maxPassengers)]],
      minPrice: [],
      maxPrice: []
    });
  }

  findVehicles() {
    if (this.reservationForm.invalid) {
      return;
    }

    const parameters = this.reservationForm.value;
    parameters.rentACarId = this.rentACar.id;
    parameters.startDay = new Date(Date.UTC(parameters.startDay.getFullYear(),
                                            parameters.startDay.getMonth(),
                                            parameters.startDay.getDate()));
    parameters.endDay = new Date(Date.UTC(parameters.endDay.getFullYear(),
                                          parameters.endDay.getMonth(),
                                          parameters.endDay.getDate()));
    const fuelTypes = [];
    const transmissionTypes = [];
    this.fuelTypesList.selectedOptions.selected.map(elem => fuelTypes.push(elem.value));
    this.transmissionTypesList.selectedOptions.selected.map(elems => transmissionTypes.push(elems.value));
    parameters.fuelTypes = fuelTypes;
    parameters.transmissionTypes = transmissionTypes;
    this.rentACarService.getAvailableVehicles(parameters).subscribe(
      (vehicles: any[]) => {
        this.vehicles = vehicles;
        console.log(this.vehicles);
      },
      (error) => {
        alert('Error!');
      }
    );
  }

  finish() {
    console.log('reserved!');
  }
}
