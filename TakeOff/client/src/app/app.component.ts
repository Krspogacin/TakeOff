import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from './services/authentication/authentication.service';
import { MatSnackBar, MatDialog } from '@angular/material';
import { SysAdminDialogComponent } from './components/sys-admin-dialog/sys-admin-dialog.component';
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'AngularTakeOff';
  username: string;
  profileImage: string;
  userRole: string;

  constructor(private authenticationService: AuthenticationService,
    public snackBar: MatSnackBar, public dialog: MatDialog) { }

  ngOnInit() {
    this.authenticationService.onSubject.subscribe(
      () => {
        if (this.authenticationService.getUsername()) {
          this.username = this.authenticationService.getUsername();
          this.profileImage = this.authenticationService.getProfileImage();
          this.userRole = this.authenticationService.getAuthority();
        } else {
          this.username = null;
        }
      }
    );
    this.username = this.authenticationService.getUsername();
    this.profileImage = this.authenticationService.getProfileImage();
    this.userRole = this.authenticationService.getAuthority();
  }

  open() {
    const dialogRef = this.dialog.open(SysAdminDialogComponent, { disableClose: true });
  }

  changePassword() {
    this.dialog.open(ChangePasswordDialogComponent,
    {
      data: {'enabled': this.authenticationService.isUserEnabled()},
      disableClose: true,
      autoFocus: true,
      width: '40%'
    });
  }

  showSnackBar(message: string) {
    if (!message) {
      return;
    }
    const snackBarRef = this.snackBar.open(message, 'Dissmiss', { verticalPosition: 'top', duration: 4000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }

  sortArray(array: any, sortBy: string, asc: boolean) {
    if (!Array.isArray(array) || array.length === 0) {
      return [];
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

  calculateDistance(flight) {
    const lat1 = flight.takeOffLocation.latitude;
    const lon1 = flight.takeOffLocation.longitude;
    const lat2 = flight.landingLocation.latitude;
    const lon2 = flight.landingLocation.longitude;

    const p = 0.017453292519943295;    // Math.PI / 180
    const c = Math.cos;
    const a = 0.5 - c((lat2 - lat1) * p) / 2 +
      c(lat1 * p) * c(lat2 * p) *
      (1 - c((lon2 - lon1) * p)) / 2;

    return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
  }
}
