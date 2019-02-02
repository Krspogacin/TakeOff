import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nameFilter'
})
export class NameFilterPipe implements PipeTransform {

  transform(rentACars: [], searchName: string) {
    if (!rentACars || !searchName || searchName.length === 0) {
      return rentACars;
    }
    searchName = searchName.toLowerCase();
    return rentACars.filter((elem: any) => elem.name.toLowerCase().includes(searchName));
  }

}
