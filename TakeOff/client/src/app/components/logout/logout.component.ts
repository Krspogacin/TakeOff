import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  thereIsLoggedInUser: boolean;

  constructor(private authenticationService: AuthenticationService,
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

  logout() {
    this.authenticationService.logout().subscribe(
      () => {
        this.authenticationService.removeUserState();
        this.router.navigate(['/']);
        this.appComponent.showSnackBar('Logged out successfully!');
      },
      () => {
      }
    );
  }
}
