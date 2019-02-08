import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'licationAndName'
})
export class LicationAndNamePipe implements PipeTransform {

  transform(hotels: [], nameOrLocation: string) {
    if (!hotels || !nameOrLocation){
      return hotels;
    }
    nameOrLocation = nameOrLocation.toLowerCase();
    return hotels.filter((elem:any) => elem.name.toLowerCase().includes(nameOrLocation) ||
                                       elem.location.country.toLowerCase().includes(nameOrLocation) ||
                                       elem.location.city.toLowerCase().includes(nameOrLocation));
  }

}
