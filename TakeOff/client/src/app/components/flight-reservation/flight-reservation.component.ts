import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatSelectionList, MatSelectionListChange, MatListOption } from '@angular/material';
import { FormBuilder } from '@angular/forms';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
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
  loading = true;

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
          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              // filter only accepted friend requests
              this.friends = friendData.filter((x: any) => x.accepted);
              this.seats = Array(this.data).fill(0).map((x, i) => i);
              this.loading = false;
            });
        });

    }
  }

  selectionChange(option: MatListOption) {
    const selected = this.friendList.selectedOptions.selected.length;
    if (selected === this.seats.length) {
      option.selected = false;
    }

    console.log(this.friendList.selectedOptions.selected.map(item => item.value));

  }

}
