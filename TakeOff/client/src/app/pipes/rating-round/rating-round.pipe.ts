import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ratingRound'
})
export class RatingRoundPipe implements PipeTransform {

  transform(value: number): any {
    return Math.round(value / 0.1) * 0.1;
  }
}
