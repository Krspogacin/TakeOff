import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AirCompanyComponent } from './components/air-company/air-company.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HotelComponent } from './components/hotel/hotel.component';
import { AddHotelModalComponent} from './components/add-hotel-modal/add-hotel-modal.component';

const routes: Routes = [
  { path: 'companies/:id', component: AirCompanyComponent },
  { path: 'hotels/:id', component : HotelComponent },
  { path: 'hotels', component : HotelComponent },
  { path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const RoutingComponents = [AirCompanyComponent,HotelComponent,AddHotelModalComponent];
