import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSelectionList } from '@angular/material';
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
  rentACar: any;
  landingDate: Date;
  choosenItem: any = null;
  vehicles: any[] = [];
  minPassengers = 1;
  maxPassengers = 7;
  fuelTypes: any[] = [];
  transmissionTypes: any[] = [];
  vehiclesLoaded = false;
  currentDate = new Date();
  error = false;
  @ViewChild('fuelTypesList') fuelTypesList: MatSelectionList;
  @ViewChild('transmissionTypesList') transmissionTypesList: MatSelectionList;

  constructor(private rentACarService: RentACarService,
              private vehicleService: VehicleService,
              private dialogRef: MatDialogRef<VehicleReservationDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    if (!this.data.rentACar || !this.data.landingDate || !(this.data.landingDate instanceof Date)) {
      this.error = true;
      return;
    }
    this.rentACar = this.data.rentACar;
    this.landingDate = this.data.landingDate;

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
    parameters.startDate = new Date(Date.UTC(parameters.startDate.getFullYear(),
                                             parameters.startDate.getMonth(),
                                             parameters.startDate.getDate()));
    parameters.endDate = new Date(Date.UTC(parameters.endDate.getFullYear(),
                                           parameters.endDate.getMonth(),
                                           parameters.endDate.getDate()));
    const fuelTypes = [];
    const transmissionTypes = [];
    this.fuelTypesList.selectedOptions.selected.map(elem => fuelTypes.push(elem.value));
    this.transmissionTypesList.selectedOptions.selected.map(elems => transmissionTypes.push(elems.value));
    parameters.fuelTypes = fuelTypes;
    parameters.transmissionTypes = transmissionTypes;
    this.rentACarService.getAvailableVehicles(parameters).subscribe(
      (vehicles: any[]) => {
        this.vehicles = vehicles;
        this.vehiclesLoaded = true;
      },
      (error) => {
        alert('Error! Vehicles could not be loaded!');
      }
    );
  }

  finish() {
    if (this.choosenItem) {
      const choosenItem = this.choosenItem;
      delete choosenItem['rating'];
      const reservationStartDate: Date = this.reservationForm.get('startDate').value;
      choosenItem.reservationStartDate = new Date(Date.UTC(reservationStartDate.getFullYear(),
                                                           reservationStartDate.getMonth(),
                                                           reservationStartDate.getDate()));
      const reservationEndDate: Date = this.reservationForm.get('endDate').value;
      choosenItem.reservationEndDate = new Date(Date.UTC(reservationEndDate.getFullYear(),
                                                          reservationEndDate.getMonth(),
                                                          reservationEndDate.getDate()));
      this.dialogRef.close(this.choosenItem);
    }
  }
}
