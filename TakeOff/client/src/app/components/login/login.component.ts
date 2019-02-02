import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoginDialogComponent } from '../login-dialog/login-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  thereIsLoggedInUser: boolean;

  constructor(private authenticationService: AuthenticationService,
              public dialog: MatDialog,
              private router: Router,
              private appComponent: AppComponent) { }

  ngOnInit(): void {
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

  openLoginDialog() {
    const dialogRef = this.dialog.open(LoginDialogComponent,
      {
        data: undefined,
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });
    dialogRef.afterClosed().subscribe(
      (data) => {
        if (data) {
          this.authenticationService.setUserState(data);
          this.router.navigate(['/']);
          this.appComponent.showSnackBar('Logged in successfully!');
        }
      }
    );
  }
}
