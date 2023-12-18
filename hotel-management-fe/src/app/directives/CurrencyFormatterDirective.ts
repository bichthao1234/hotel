import {Directive, ElementRef, HostListener, OnInit} from '@angular/core';
import {NgControl} from '@angular/forms';

@Directive({
  selector: '[appCurrencyFormatter]'
})
export class CurrencyFormatterDirective implements OnInit {

  private el: HTMLInputElement;

  constructor(
    private elementRef: ElementRef,
    private control: NgControl
  ) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit() {
    this.el.value = this.format(this.el.value);
  }

  @HostListener('focus', ['$event.target.value'])
  onFocus(value: string) {
    this.el.value = this.parse(value); // opossite of transform
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur(value: string) {
    // @ts-ignore
    this.control.control.setValue(this.parse(value));
    this.el.value = this.format(value);
  }

  parse(value: string): string {
    return value.replace(/\./g, '');
  }

  format(value: string): string {
    return Number(value).toLocaleString('vi-VN');
  }
}
