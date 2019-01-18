import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoginDialogComponent } from '../login-dialog/login-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  thereIsLoggedInUser: boolean;

  constructor(private authenticationService: AuthenticationService, public dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    if (this.authenticationService.getAccessToken()) {
      this.thereIsLoggedInUser = true;
    } else {
      this.thereIsLoggedInUser = false;
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
          this.thereIsLoggedInUser = true;
          // this.router.navigate(['/']);
        }
      }
    );
  }

  logout() {
    this.authenticationService.logout().subscribe(
      () => {
        this.authenticationService.removeUserState();
        this.thereIsLoggedInUser = false;
        this.router.navigate(['/']);
      },
      (error) => {
      }
    );
  }
}
