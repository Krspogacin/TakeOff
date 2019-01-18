import { Component, OnInit, Inject} from '@angular/core';
import { MatDialog } from '@angular/material';
import { RegistrationDialogComponent } from 'src/app/components/registration-dialog/registration-dialog.component';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  constructor(private registrationService: RegistrationService, public dialog: MatDialog, private router: Router) { }

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
            (user) => {
              this.router.navigate(['/users/successful_registration']);
            });
        }
      }
    );
  }
}
