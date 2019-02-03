import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';

@Component({
  selector: 'app-rent-a-car-main-service-dialog',
  templateUrl: './rent-a-car-main-service-dialog.component.html',
  styleUrls: ['./rent-a-car-main-service-dialog.component.css']
})
export class RentACarMainServiceDialogComponent implements OnInit {

  rentACarMainServicesForm: FormGroup;
  update = true;
  rentACarMainService: any;
  vehicles: any[] = [];

  constructor(private rentACarService: RentACarService,
              private dialogRef: MatDialogRef<RentACarMainServiceDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

    ngOnInit(): void {
      this.rentACarMainService = this.data.rentACarMainService;
      this.vehicles = this.data.vehicles;
      if (!this.rentACarMainService) {
        this.rentACarMainService = {'name': '', 'startDay': '', 'endDay': ''};
        this.update = false;
      }
      this.rentACarMainServicesForm = this.formBuilder.group({
         name: [this.rentACarMainService.name, Validators.required],
         startDay: [this.rentACarMainService.startDay, Validators.required],
         endDay: [this.rentACarMainService.endDay]
      });

      if (this.vehicles) {
        for (const vehicle of this.vehicles) {
          this.rentACarMainServicesForm.addControl(vehicle.id + '', this.formBuilder.control('', Validators.required));
        }
      }
    }

    submitForm() {
      const nameControl: AbstractControl = this.rentACarMainServicesForm.get('name');
      this.rentACarService.checkMainServiceName(this.rentACarMainService.id, nameControl.value).subscribe(
        () => {
          const rentACarMainService = this.rentACarMainServicesForm.value;
          rentACarMainService.id = this.rentACarMainService.id;

          const vehiclePrices = [];
          if (this.vehicles) {
            // tslint:disable-next-line:forin
            for (const vehicle of this.vehicles) {
              delete rentACarMainService[vehicle.id];
              const vehiclePrice = {'price' : this.rentACarMainServicesForm.get(vehicle.id + '').value,
                                    'vehicle' : vehicle,
                                    'rentACarMainServiceDTO' : rentACarMainService};
              vehiclePrices.push(vehiclePrice);
            }
          }

          this.dialogRef.close({'rentACarMainService': rentACarMainService, 'vehiclePrices': vehiclePrices});
        },
        () => {
          nameControl.setErrors({ nameExists: true });
          const element = document.getElementById('scrollId');
          element.scrollTo(0, 0);
        }
      );
    }
}
