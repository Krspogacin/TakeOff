import { Component, OnInit, Inject, AfterViewInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';

declare let require: any;

@Component({
  selector: 'app-add-entity-dialog',
  templateUrl: 'add-entity-dialog.component.html',
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class AddEntityDialogComponent implements OnInit, AfterViewInit{
  
  passwordMinLength = 8;
  passwordMaxLength = 32;
  usernameMinLength = 5;
  usernameMaxLength = 25;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  location: any = {};

  constructor(private dialogRef: MatDialogRef<AddEntityDialogComponent>,
              private formBuilder: FormBuilder,
              private registrationService: RegistrationService,
              private hotelService: HotelService,
              private airCompanyService: AirCompanyService,
              private rentACarService: RentACarService,
              @Inject(MAT_DIALOG_DATA) public entityNo: any) { }

    ngOnInit() {
      this.firstFormGroup = this.formBuilder.group({
        name: ['',Validators.required],
        address: ['',Validators.required],
        description: [],
      });

      this.secondFormGroup = this.formBuilder.group({
        username : ['', [Validators.required,
                         Validators.minLength(this.usernameMinLength),
                         Validators.maxLength(this.usernameMaxLength)]],
        password:  ['', [Validators.required,
                         Validators.minLength(this.passwordMinLength),
                         Validators.maxLength(this.passwordMaxLength)]],
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
        this.firstFormGroup.controls.address.setValue('');
      });
    }

    addEntity(){
      this.validateByUsername();
    }

    validateByUsername() {
      const usernameControl: AbstractControl = this.secondFormGroup.get('username');
      this.registrationService.checkUserByUsername(usernameControl.value).subscribe(
        (data) => {
          usernameControl.setErrors(null);
        },
        (error) => {
          usernameControl.setErrors({ usernameExists: true });
        },
        () => {
          this.addEntityIfUserIsValid();
        });
    }

    addEntityIfUserIsValid() {
      if (this.firstFormGroup.valid && this.secondFormGroup.valid) {
        const entity = this.firstFormGroup.value;
        delete entity['address'];
        entity.location = this.location;
        const administrator = this.secondFormGroup.value;
        if(this.entityNo == 0){
          this.airCompanyService.addCompany(entity).subscribe(
            (data) => {
              administrator.airCompanyDTO = data;
              this.registrationService.addAdmin(administrator).subscribe(
                (admin) => {
                }
              )
              this.dialogRef.close(data);
            }
          );
        }else if(this.entityNo == 1){
          this.hotelService.addHotel(entity).subscribe(
            (data) => {
              administrator.hotelDTO = data;
              this.registrationService.addAdmin(administrator).subscribe(
                (admin) => {
                }
              )
              this.dialogRef.close(data);
            }
          );
        }else{
          this.rentACarService.addRentACar(entity).subscribe(
            (data) => {
              administrator.rentACarDTO = data;
              this.registrationService.addAdmin(administrator).subscribe(
                (admin) => {
                }
              )
              this.dialogRef.close(data);
            }
          );
        }
      }
    }
}

