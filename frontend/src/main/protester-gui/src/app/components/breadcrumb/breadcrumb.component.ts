import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter, mergeMap} from "rxjs/operators";
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

  component_id: number;
  component_name: string;

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
      .pipe(
        filter(event => event instanceof NavigationEnd),
        mergeMap(() => this.menuItems = this.createBreadcrumbs(this.activatedRoute.root)),
        filter(menuItem => menuItem.label === 'Project'),
        mergeMap(() => this.projectService.getProjectById(this.component_id)))
      .subscribe(value => {
        console.log(value.projectName);
        this.menuItems.find(mi => mi.label === 'Project').label = value.projectName;
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

    this.component_id = route.snapshot.params['id'];

    let label = child.snapshot.data[BreadcrumbComponent.ROUTE_DATA_BREADCRUMB];
    if (!(label === null || label === undefined)) {
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
