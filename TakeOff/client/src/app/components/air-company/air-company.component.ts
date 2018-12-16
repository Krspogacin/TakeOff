import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

@Component({
  selector: 'app-air-company',
  templateUrl: './air-company.component.html',
  styleUrls: ['./air-company.component.css']
})
export class AirCompanyComponent implements OnInit {

  company = {};

  constructor(private airCompanyService: AirCompanyService, private route: ActivatedRoute) { }

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    console.log('id: ' + id);
    if (!isNaN(id)) {
      this.airCompanyService.getCompanyById(id).subscribe(
        (data) => {
          this.company = data;
        },
        error => {
          // handle not found error
        });
    }
  }

}
