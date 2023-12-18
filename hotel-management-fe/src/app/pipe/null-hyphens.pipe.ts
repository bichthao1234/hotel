import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nullHyphens'
})
export class NullHyphensPipe implements PipeTransform {

  transform(value: any): any {
    return value ?? '-';
  }

}
