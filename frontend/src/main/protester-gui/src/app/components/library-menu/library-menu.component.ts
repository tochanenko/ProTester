import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-library-menu',
  templateUrl: './library-menu.component.html',
  styleUrls: ['./library-menu.component.css']
})
export class LibraryMenuComponent implements OnInit {

  constructor(private router: Router) {
    this.router.navigateByUrl('/library').then();
  }


  ngOnInit(): void {
  }

}
