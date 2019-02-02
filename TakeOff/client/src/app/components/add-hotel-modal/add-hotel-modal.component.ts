import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef} from '@angular/material';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { HotelService } from 'src/app/services/hotel/hotel.service';

@Component({
  selector: 'app-add-hotel-modal',
  templateUrl: 'add-hotel-modal.component.html',
})
export class AddHotelModalComponent implements OnInit{
  
  passwordMinLength = 8;
  passwordMaxLength = 32;
  usernameMinLength = 5;
  usernameMaxLength = 25;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;

  constructor(private dialogRef: MatDialogRef<AddHotelModalComponent>,
              private formBuilder: FormBuilder,
              private registrationService: RegistrationService,
              private hotelService: HotelService) { }

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

    addHotel(){
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
          this.addHotelIfUserIfValid();
        });
    }

    addHotelIfUserIfValid() {
      if (this.firstFormGroup.valid && this.secondFormGroup.valid) {
        const hotel = this.firstFormGroup.value;
        const administrator = this.secondFormGroup.value;
        this.hotelService.addHotel(hotel).subscribe(
          (data:{}) => {
            administrator.hotelDTO = data;
            this.registrationService.addAdmin(administrator).subscribe(
              (admin:{}) => {
                console.log(admin);
              }
            )
            this.dialogRef.close(data);
          }
        )
      }
    }


}
