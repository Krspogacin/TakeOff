import { Component, OnInit, Inject} from '@angular/core';
import { MatDialog } from '@angular/material';
import { RegistrationDialogComponent } from 'src/app/components/registration-dialog/registration-dialog.component';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  thereIsLoggedInUser: boolean;

  constructor(private authenticationService: AuthenticationService,
              private registrationService: RegistrationService,
              public dialog: MatDialog,
              private router: Router) { }

  ngOnInit() {
    this.authenticationService.onSubject.subscribe(
      (data) => {
        if (data.value) {
          this.thereIsLoggedInUser = true;
        } else {
          this.thereIsLoggedInUser = false;
        }
      }
    );
    if (this.authenticationService.getAccessToken()) {
      this.thereIsLoggedInUser = true;
    }
  }

  openRegistrationDialog() {
    const dialogRef = this.dialog.open(RegistrationDialogComponent,
      { data: undefined,
        disableClose: true,
        autoFocus : true,
        width: '40%' });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          this.registrationService.registerUser(data).subscribe(
            () => {
              alert('You have successfully registered! Congratulations! Open the email and confirm that it is you!');
              this.router.navigate(['/']);
            }
          );
        }
      }
    );
  }
}
