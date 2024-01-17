import { Directive, HostListener } from '@angular/core';
import slugify from 'slugify';

@Directive({
    selector: '[appAlphanumeric]'
})
export class AlphanumericDirective {

    @HostListener('input', ['$event']) onInput(event: KeyboardEvent): void {
        const input = event.target as HTMLInputElement;
        const newText = slugify(input.value, { lower: true, remove: /[\*+~.,()'"!:@]/g, locale: 'en' })
        input.value = newText;
    }
}
