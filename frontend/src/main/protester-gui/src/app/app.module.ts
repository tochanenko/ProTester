import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule, DatePipe} from "@angular/common";
import {AuthInterceptor} from "./services/auth/auth.interceptor";
import {MatIconRegistry} from '@angular/material/icon';
import {MaterialModule} from "./services/material.module";
import {HeaderComponent} from './components/header/header.component';
import {NgxMatSelectSearchModule} from "ngx-mat-select-search";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {BottomSheetComponent} from './components/bottom-sheet/bottom-sheet.component';
import {MAT_BOTTOM_SHEET_DEFAULT_OPTIONS} from "@angular/material/bottom-sheet";
import {MatTabsModule} from "@angular/material/tabs";
import {SharedModule} from "./modules/shared/shared.module";
import {ProjectsMenuModule} from "./modules/projects-menu/projects-menu.module";
import {LibrariesMenuModule} from "./modules/libraries-menu/libraries-menu.module";
import {BreadcrumbComponent} from './components/breadcrumb/breadcrumb.component';
import {BreadcrumbModule} from "primeng/breadcrumb";
import {TestScenarioModule} from "./modules/projects-menu/test-scenario/test-scenario.module";
import { DialogUtilComponent } from './components/dialog-util/dialog-util.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    BottomSheetComponent,
    BreadcrumbComponent,
    DialogUtilComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialModule,
    NgxMatSelectSearchModule,
    MatCheckboxModule,
    NgxMatSelectSearchModule,
    ScrollingModule,
    MatTabsModule,
    SharedModule,
    ProjectsMenuModule,
    LibrariesMenuModule,
    BreadcrumbModule,
    TestScenarioModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}, {provide: MAT_BOTTOM_SHEET_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}} , MatIconRegistry, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule {
}
