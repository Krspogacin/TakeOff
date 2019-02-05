import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nameFilter'
})
export class NameFilterPipe implements PipeTransform {

  transform(rentACars: [], searchName: string, searchCountry: string, searchCity: string) {
    if (!rentACars || !searchName || !searchCountry || !searchCity) {
      if (!(searchName === '' || searchCountry === '' || searchCity === '')) {
        return rentACars;
      }
    }
    searchName = searchName.toLowerCase();
    searchCountry = searchCountry.toLowerCase();
    searchCity = searchCity.toLowerCase();
    return rentACars.filter((elem: any) => elem.name.toLowerCase().includes(searchName) &&
                                           elem.location.country.toLowerCase().includes(searchCountry) &&
                                           elem.location.city.toLowerCase().includes(searchCity));
  }
}
