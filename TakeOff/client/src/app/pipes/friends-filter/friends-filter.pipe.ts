import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'friendsFilter'
})
export class FriendsFilterPipe implements PipeTransform {

  transform(users: [], searchValue: string): any {
    if (!users || !searchValue) {
      return [];
    }

    searchValue = searchValue.trim().toLowerCase();
    if (searchValue.length === 0) {
      return [];
    }

    return users.filter((element: any) => (element.firstName + ' ' + element.lastName).toLowerCase().indexOf(searchValue) === 0);
  }

}
