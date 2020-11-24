import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  image = 'assets/logo.png';
  isWhiteTheme = true;

  constructor() {
  }

  ngOnInit(): void {
  }

  changeTheme(): void {
    this.isWhiteTheme = !this.isWhiteTheme;
    //change theme
  }
}
