import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatListOption, MatSelectionList, MAT_DIALOG_DATA } from '@angular/material';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-flight-reservation',
  templateUrl: './flight-reservation.component.html',
  styleUrls: ['./flight-reservation.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class FlightReservationComponent implements OnInit {

  seats = [];
  user: any;
  friends = [];
  formObject = {};
  passengerForm: FormGroup;
  fieldNumber = 4;
  total = 0;
  currency = '€';

  @ViewChild(MatSelectionList) friendList: MatSelectionList;

  constructor(private userService: UserService, private authService: AuthenticationService,
    private dialogRef: MatDialogRef<FlightReservationComponent>,
    private formBuilder: FormBuilder, @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {
      this.userService.getUser(username).subscribe(
        (data) => {
          this.user = data;
          this.passengerForm.controls['0-0'].setValue(this.user.firstName);
          this.passengerForm.controls['0-1'].setValue(this.user.lastName);
          this.passengerForm.controls['0-2'].setValue(this.user.dateOfBirth);
          this.passengerForm.controls['0-3'].setValue(this.user.phoneNumber);

          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              // filter only accepted friends and exactly those objects
              friendData.forEach((element: any) => {
                if (element.accepted) {
                  if (element.user1.id === this.user.id) {
                    this.friends.push(element.user2);
                  } else {
                    this.friends.push(element.user1);
                  }
                }
              });
            });
        });

      this.seats = Array(this.data.seats).fill(0).map((x, i) => i);
      this.total = this.data.total;

      for (let i = 0; i < this.seats.length; i++) {
        for (let j = 0; j < this.fieldNumber; j++) {
          let control;
          // only for date put disabled = true and without validators
          if (j === 2) {
            control = new FormControl({ value: '', disabled: true });
          } else {
            const validators = [Validators.required];
            if (j === 3) {
              // special validator for phone number
              validators.push(Validators.pattern('^[0-9]{3}(-)[0-9]{3}$'));
            }
            control = new FormControl('', validators);
          }
          this.formObject[i + '-' + j] = control;
        }
      }

      this.passengerForm = new FormGroup(this.formObject);
    }
  }

  selectionChange(option: MatListOption) {
    // check if all seats are already taken
    let selected = this.friendList.selectedOptions.selected.length;
    if (selected === this.seats.length) {
      option.selected = false;
    }

    // reseting form value
    for (let i = 1; i < this.seats.length; i++) {
      for (let j = 0; j < this.fieldNumber; j++) {
        this.passengerForm.controls[i + '-' + j].setValue('');
      }
    }

    // setting new values
    selected = this.friendList.selectedOptions.selected.length;
    for (let i = 0; i < selected; i++) {
      const id = this.friendList.selectedOptions.selected[i].value;
      const user = this.friends[id];
      const values = [user.firstName, user.lastName, user.dateOfBirth, user.phoneNumber];
      for (let j = 0; j < this.fieldNumber; j++) {
        this.passengerForm.controls[i + 1 + '-' + j].setValue(values[j]);
      }
    }

  }

  finishReservation() {

    const friends = [this.user];
    if (this.friendList) {
      const selected = this.friendList.selectedOptions.selected.map(x => x.value);
      for (const x of selected) {
        friends.push(this.friends[x]);
      }

      if (friends.length < this.seats.length) {
        for (let i = friends.length; i < this.seats.length; i++) {
          friends.push(null);
        }
      }
    }

    this.dialogRef.close(friends);

  }

}
