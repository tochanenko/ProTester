import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {StorageService} from "../../services/auth/storage.service";
import {AuthService} from "../../services/auth/auth.service";
import {Subscription} from "rxjs";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  image = 'assets/logo.png';
  isWhiteTheme = true;
  user: User = new User();
  isAdmin: boolean = false;
  links = [
    {
      'link': 'projects-menu',
      'label': 'Projects'
    },
    {
      'link': 'libraries-menu',
      'label': 'Libraries'
    }
  ];
  activeLink = this.links[0];
  userSubscription: Subscription;
  navigationSubscription: Subscription;

  constructor(private storageService: StorageService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.userSubscription = this.storageService.currentUser.subscribe(user => {
      this.user = user;
      this.isAdmin = user != null && user.role === 'ADMIN';
    });
    this.navigationSubscription = this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        switch (event.urlAfterRedirects.split("/")[1]) {
          case 'projects-menu':
            this.activeLink = this.links[0];
            break;
          case 'libraries-menu':
            this.activeLink = this.links[1];
            break;
        }
      }
    });
  }

  changeTheme(): void {
    this.isWhiteTheme = !this.isWhiteTheme;
    //change theme
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/account/login').then();
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
    this.navigationSubscription.unsubscribe();
  }
}
