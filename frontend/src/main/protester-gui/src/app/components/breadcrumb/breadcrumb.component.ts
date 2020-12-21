import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter, first} from "rxjs/operators";
import {MenuItem} from "primeng/api";
import {Subscription} from "rxjs";
import {User} from "../../models/user.model";
import {StorageService} from "../../services/auth/storage.service";
import {CompoundManageService} from "../../services/compound-manage.service";
import {ProjectService} from "../../services/project.service";

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit, OnDestroy {
  static readonly ROUTE_DATA_BREADCRUMB = 'breadcrumb';
  readonly home = {icon: 'pi pi-home', url: '#/projects-menu'};
  menuItems: MenuItem[];

  userSubscription: Subscription;
  navigationSubscription: Subscription;

  compoundSubscription: Subscription;
  projectSubscription: Subscription;
  librarySubscription: Subscription;

  user: User = new User();

  constructor(
    private storageService: StorageService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private compoundService: CompoundManageService,
    private projectService: ProjectService) {
  }

  ngOnInit(): void {
    this.userSubscription = this.storageService.currentUser.subscribe(user => this.user = user);
    this.navigationSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        console.log("------------Start----------------")
        this.menuItems = this.createBreadcrumbs(this.activatedRoute.root);
        console.log("-------------End-----------------")
      });
  }

  private createBreadcrumbs(route: ActivatedRoute, url: string = '#', breadcrumbs: MenuItem[] = []): MenuItem[] {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return breadcrumbs;
    }

    const child = children[0];
    const routeURL: string = child.snapshot.url.map(segment => segment.path).join('/');
    if (routeURL !== '') {
      url += `/${routeURL}`;
    }

    const id = route.snapshot.params['id'];

    let label = child.snapshot.data[BreadcrumbComponent.ROUTE_DATA_BREADCRUMB];
    if (!(label === null || label === undefined)) {
      let addition: string = '';
      switch (label) {
        case 'Compound':
          if (this.compoundSubscription != undefined) {
            this.compoundSubscription.unsubscribe();
          }
          this.compoundSubscription = this.compoundService.getCompoundById(id)
            .pipe(first())
            .subscribe(component => addition = component.name);
          break;
        case 'Project':
          if (this.projectSubscription != undefined) {
            this.projectSubscription.unsubscribe();
          }
          this.projectSubscription = this.projectService.getProjectById(id)
            .pipe(first())
            .subscribe(component => addition = component.projectName);
      }

      label += addition;

      breadcrumbs.push({label, url});
    }

    return this.createBreadcrumbs(child, url, breadcrumbs);
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
    this.navigationSubscription.unsubscribe();

    if (this.compoundSubscription != undefined) {
      this.compoundSubscription.unsubscribe();
    }
    if (this.projectSubscription != undefined) {
      this.projectSubscription.unsubscribe();
    }
    if (this.librarySubscription != undefined) {
      this.librarySubscription.unsubscribe();
    }
  }
}
