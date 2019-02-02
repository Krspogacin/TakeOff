import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { PageEvent } from '@angular/material';

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

  constructor(private companyService: AirCompanyService) { }

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
    // const dialogRef = this.dialog.open(AddHotelModalComponent, { disableClose: true });

    // dialogRef.afterClosed().subscribe(result => {
    //   console.log('Hotel:', result);
    //   this.HotelService.addHotel(result).subscribe(
    //     (data:{}) => {
    //       this.hotels.push(data);
    //     }
    //   )
    // });
  }

  pageFunction(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
  }

}
