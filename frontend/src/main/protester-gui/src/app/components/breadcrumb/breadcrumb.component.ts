import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";
import {MenuItem} from "primeng/api";
import {Subscription} from "rxjs";
import {User} from "../../models/user.model";
import {StorageService} from "../../services/auth/storage.service";
import {CompoundManageService} from "../../services/compound-manage.service";
import {ProjectService} from "../../services/project.service";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {TestScenarioService} from "../../services/test-scenario/test-scenario-service";
import {UserService} from "../../services/user/user.service";

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
    private libraryService: LibraryManageService,
    private projectService: ProjectService,
    private scenarioService: TestScenarioService,
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.userSubscription = this.storageService.currentUser.subscribe(user => this.user = user);
    this.navigationSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(async () => {
        this.menuItems = await this.createBreadcrumbs(this.activatedRoute.root)
      });
  }

  private async createBreadcrumbs(route: ActivatedRoute, url: string = '#', breadcrumbs: MenuItem[] = []) {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return breadcrumbs;
    }

    const child = children[0];
    const routeURL: string = child.snapshot.url.map(segment => segment.path).join('/');
    if (routeURL !== '') {
      url += `/${routeURL}`;
    }

    let id = route.snapshot.params['id'];

    let label = child.snapshot.data[BreadcrumbComponent.ROUTE_DATA_BREADCRUMB];
    if (!(label === null || label === undefined)) {
      switch (label) {
        case 'Project':
          label += ' ' + await this.getProjectNameById(id);
          break;
        case 'Compound':
          label = ' ' + await this.getCompoundNameById(id);
          break;
        case 'Library':
          label = ' ' + await this.getLibraryNameById(id);
          break;
        case 'Scenario':
          label = ' ' + await this.getScenarioNameById(id);
          break;
        case 'User':
          label = ' ' + await this.getUserNameById(id);
          break;
      }
      breadcrumbs.push({label, url});
    }

    return this.createBreadcrumbs(child, url, breadcrumbs);
  }

  async getProjectNameById(id: number) {
    let resp = await this.projectService.getProjectById(id).toPromise();
    return resp.projectName;
  }

  async getCompoundNameById(id: number) {
    let resp = await this.compoundService.getCompoundById(id).toPromise();
    return resp.name;
  }

  async getLibraryNameById(id: number) {
    let resp = await this.libraryService.getLibraryById(id).toPromise();
    return resp.name;
  }

  async getScenarioNameById(id: number) {
    let resp = await this.scenarioService.getById(id).toPromise();
    return resp.name;
  }

  async getUserNameById(id: number) {
    let resp = await this.userService.getUserById(id).toPromise();
    return resp.username;
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
    this.navigationSubscription.unsubscribe();
  }
}
