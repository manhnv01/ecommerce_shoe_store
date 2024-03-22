import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'abbreviation'
})
export class AbbreviationPipe implements PipeTransform {
  transform(value: number): string {
    let newValue = value;
    let suffix = '';
    
    if (value >= 1000000) {
      newValue = value / 1000000;
      suffix = 'tr';
    } else if (value >= 1000) {
      newValue = value / 1000;
      suffix = 'k';
    }

    return newValue % 1 === 0 ? newValue.toString() + suffix : newValue.toFixed(1) + suffix;
  }
}
