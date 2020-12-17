import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-invalid-token',
  templateUrl: './invalid-token.component.html',
  styleUrls: ['./invalid-token.component.css']
})
export class InvalidTokenComponent implements OnInit {

  image = 'assets/logo.png';

  constructor() { }

  ngOnInit(): void {
  }

}
