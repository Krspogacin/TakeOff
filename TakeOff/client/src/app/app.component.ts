import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from './services/authentication/authentication.service';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'AngularTakeOff';
  username: string;
  profileImage: string;

  constructor(private authenticationService: AuthenticationService,
              public snackBar: MatSnackBar) { }

  ngOnInit() {
    this.authenticationService.onSubject.subscribe(
      (data) => {
        if (data.value) {
          this.username = data.value.username;
          this.profileImage = data.value.image;
        } else {
          this.username = null;
        }
      }
    );
    this.username = this.authenticationService.getUsername();
    this.profileImage = this.authenticationService.getProfileImage();
  }

  showSnackBar(message: string) {
    if (!message) {
      return;
    }
    const snackBarRef = this.snackBar.open(message, 'Dissmiss', { duration: 3000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }

  sortArray(array: any, sortBy: string, asc: boolean) {
    if (!Array.isArray(array) || array.length === 0) {
      return;
    }
    array.sort((a: any, b: any) => {
      let val1 = a[sortBy];
      let val2 = b[sortBy];
      if (typeof a[sortBy] === 'string') {
        val1 = val1.toLowerCase();
        val2 = val2.toLowerCase();
      }
      if (val1 < val2) {
        return asc ? -1 : 1;
      } else if (val1 > val2) {
        return asc ? 1 : -1;
      } else {
        return 0;
      }
    });
    return array;
  }
}
