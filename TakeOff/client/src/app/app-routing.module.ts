import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AirCompanyComponent } from './components/air-company/air-company.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

import { HotelComponent } from './components/hotel/hotel.component';
import { AddHotelModalComponent} from './components/add-hotel-modal/add-hotel-modal.component';
import { FlightComponent } from './components/flight/flight.component';
import { RegistrationComponent } from './components/registration/registration.component';

const routes: Routes = [
  { path: 'companies/:id', component: AirCompanyComponent },
  { path: 'flights/:id', component: FlightComponent },
  { path: 'hotels', component : HotelComponent },
  { path: 'users/registration', component: RegistrationComponent },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [AirCompanyComponent, NotFoundComponent, RegistrationComponent, FlightComponent, HotelComponent];
