import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service'
import { MatDialog} from '@angular/material';
import { SysAdminDialogComponent } from './components/sys-admin-dialog/sys-admin-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'AngularTakeOff';
  userRole: string = null;

  constructor(private authService: AuthenticationService, public dialog: MatDialog) {}

  ngOnInit() {
    this.userRole = this.authService.getAuthority();
  }

  open(){
    const dialogRef = this.dialog.open(SysAdminDialogComponent, { disableClose: true });
  }
}
