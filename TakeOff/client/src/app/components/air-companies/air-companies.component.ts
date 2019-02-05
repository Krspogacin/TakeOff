import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { PageEvent, MatDialog } from '@angular/material';
import { AddEntityDialogComponent } from '../add-entity-dialog/add-entity-dialog.component';
import { AppComponent } from 'src/app/app.component';

declare let require: any;

@Component({
  selector: 'app-air-companies',
  templateUrl: './air-companies.component.html',
  styleUrls: ['./air-companies.component.css']
})
export class AirCompaniesComponent implements OnInit {

  companies = [];
  pageSize = 10;
  pageIndex = 0;
  message: string;

  constructor(private companyService: AirCompanyService,
              public dialog: MatDialog,
              public appComponent: AppComponent) { }

  ngOnInit() {
    this.companyService.getCompanies().subscribe(
      (data: []) => {
        this.companies = data;
      }
    );

    // const places = require('places.js');
    // const placesAutocomplete = places({
    //   appId: 'pl14EZX3IQNN',
    //   apiKey: 'ad1257b86ef3f77014a0b7f168c417f7',
    //   container: document.querySelector('#address-input')
    // });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddEntityDialogComponent,
    {
      data: 0,
      disableClose: true,
      autoFocus: true,
      width: '60%',
      height: '90%'
    });
    dialogRef.afterClosed().subscribe(
      (result) => {
        if (result) {
            this.message = 'Added successfully!';
            const airCompany = result;
            this.companies.push(airCompany);
        }
      },
      () => {
        this.message = 'Error! Rent A Car could not be added!';
      },
      () => {
        this.appComponent.showSnackBar(this.message);
      }
    );
  }

  pageFunction(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
  }

}
