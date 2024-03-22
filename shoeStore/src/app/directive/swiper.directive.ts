import { Directive, AfterViewInit, ElementRef, Input } from '@angular/core';
import Swiper, { SwiperOptions } from 'swiper';

@Directive({
  selector: '[appSwiper]'
})
export class SwiperDirective implements AfterViewInit {
  @Input() config?: SwiperOptions;

  constructor(private elementRef: ElementRef) { }

  ngAfterViewInit(): void {
    new Swiper(this.elementRef.nativeElement, this.config);
  }
}
