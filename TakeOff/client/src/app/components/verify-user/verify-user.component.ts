import { Component, OnInit } from '@angular/core';
import { RegistrationService } from 'src/app/services/registration/registration.service';
import { ActivatedRoute } from '@angular/router';

enum VerificationStatus {
  OK = 200,
  NOT_FOUND = 404,
  BAD_REQUEST = 400
}

@Component({
  selector: 'app-verify-user',
  templateUrl: './verify-user.component.html',
  styleUrls: ['./verify-user.component.css']
})
export class VerifyUserComponent implements OnInit {

  waiting = true;
  verificationStatus: VerificationStatus;

  constructor(private registrationService: RegistrationService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      const token = params['token'];
      this.registrationService.verifyUser(token).subscribe(
        () => {
          this.verificationStatus = VerificationStatus.OK;
          this.waiting = false;
        },
        (error) => {
            this.verificationStatus = error.status;
            this.waiting = false;
        });
    });
  }
}
