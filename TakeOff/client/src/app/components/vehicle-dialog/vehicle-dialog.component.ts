import { Component, OnInit, Inject} from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, FormControl} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { VehicleService } from 'src/app/services/vehicle/vehicle.service';
import { RentACarService } from 'src/app/services/rent-a-car/rent-a-car.service';

@Component({
  selector: 'app-vehicle-dialog',
  templateUrl: './vehicle-dialog.component.html',
  styleUrls: ['./vehicle-dialog.component.css']
})
export class VehicleDialogComponent implements OnInit {

  vehicleForm: FormGroup;
  currentYear: number;
  minYear = 1800;
  minSeats = 1;
  maxSeats = 7;
  minDiscount = 0;
  maxDiscount = 100;
  selectedFile: File;
  imgSrc: string;
  update = false;
  fuelTypes: any;
  transmissionTypes: any;
  vehicle: any;
  mainServices: any[] = [];
  rentACarId: number;
  vehiclePricesIds: any[] = [];

  constructor(private vehicleService: VehicleService,
              private dialogRef: MatDialogRef<VehicleDialogComponent>,
              private formBuilder: FormBuilder,
              private rentACarService: RentACarService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.vehicle = this.data.vehicle;
    this.rentACarId = this.data.id;
    this.currentYear = new Date().getFullYear();

    if (!this.vehicle) {
      this.vehicle = {'brand': '', 'model': '', 'year': '', 'fuel': '',
                      'numOfSeats': '', 'transmission': '', 'reserved': false, 'discount' : ''};
    } else {
      this.update = true;
      this.imgSrc = this.vehicle.image;
    }
    this.vehicleService.getFuelTypes().subscribe(
      (data) => {
        this.fuelTypes = data;
      }
    );
    this.vehicleService.getTransmissionTypes().subscribe(
      (data) => {
        this.transmissionTypes = data;
      }
    );

    this.vehicleForm = this.formBuilder.group({
      brand: [this.vehicle.brand, Validators.required],
      model: [this.vehicle.model, Validators.required],
      year: [this.vehicle.year, [Validators.required, Validators.min(this.minYear), Validators.max(this.currentYear)]],
      fuel: [this.vehicle.fuel, Validators.required],
      numOfSeats: [this.vehicle.numOfSeats, [Validators.required, Validators.min(this.minSeats), Validators.max(this.maxSeats)]],
      transmission: [this.vehicle.transmission, Validators.required],
      discount: [this.vehicle.discount, [Validators.min(this.minDiscount), Validators.max(this.maxDiscount)]]
    });

    if (!this.data.mainServices) {
      this.rentACarService.getMainServices(this.rentACarId).subscribe(
        (mainServices: []) => {
          this.mainServices = mainServices;
          // tslint:disable-next-line:forin
          for (const mainService of this.mainServices) {
            this.vehicleForm.addControl(mainService.name + mainService.id, this.formBuilder.control('', Validators.required));
          }
        }
      );
    } else {
      // tslint:disable-next-line:forin
      for (const i in this.data.mainServices) {
        const mainService: any = this.data.mainServices[i].rentACarMainServiceDTO;
        const price: any = this.data.mainServices[i].price;
        this.vehiclePricesIds.push(this.data.mainServices[i].id);
        this.mainServices.push(mainService);
        this.vehicleForm.addControl(mainService.name + mainService.id, this.formBuilder.control(price, Validators.required));
      }
    }
  }

  submitForm() {
    if (this.vehicleForm.valid) {
      const newVehicle = this.vehicleForm.value;
      newVehicle.id = this.vehicle.id;
      newVehicle.reserved = this.vehicle.reserved;
      newVehicle.image = this.imgSrc;

      const vehiclePrices = [];
      // tslint:disable-next-line:forin
      for (const i in this.mainServices) {
        const mainService: any = this.mainServices[i];
        delete newVehicle[mainService.name + mainService.id];
        const vehiclePrice = {'id' : this.vehiclePricesIds[i],
                              'price' : this.vehicleForm.get(mainService.name + mainService.id).value,
                              'vehicle' : newVehicle,
                              'rentACarMainServiceDTO' : mainService};
        vehiclePrices.push(vehiclePrice);
      }
      this.dialogRef.close({'vehiclePrices' : vehiclePrices, 'vehicle' : newVehicle});
    }
  }

  onFileSelected(event) {
    if (event.target.files && event.target.files[0]) {
      this.selectedFile = <File>event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.imgSrc = reader.result.toString();
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  formatLabel(value: number | null) {
    if (!value) {
      return 0 + '%';
    }

    return value + '%';
  }
}
