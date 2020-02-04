/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//  Copyright © 2018 Google. All rights reserved.

#import <GoogleMobileAds/GoogleMobileAds.h>

/// Constants used to style your template.
typedef NSString* GADTNativeTemplateStyleKey NS_STRING_ENUM;

/// The font, font color and background color for your call to action view.
/// All templates have a call to action view.
#pragma mark - Call To Action

/// Call to action font. Expects a UIFont.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyCallToActionFont;

/// Call to action font color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyCallToActionFontColor;

/// Call to action background color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyCallToActionBackgroundColor;

/// The font, font color and background color for the first row of text in the template.
/// All templates have a primary text area which is populated by the native ad's headline.
#pragma mark - Primary Text

/// Primary text font. Expects a UIFont.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyPrimaryFont;

/// Primary text font color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyPrimaryFontColor;

/// Primary text background color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyPrimaryBackgroundColor;

/// The font, font color and background color for the second row of text in the template.
/// All templates have a secondary text area which is populated either by the body of the ad,
/// or by the rating of the app.
#pragma mark - Secondary Text

/// Secondary text font. Expects a UIFont.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeySecondaryFont;

/// Secondary text font color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeySecondaryFontColor;

/// Secondary text background color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeySecondaryBackgroundColor;

/// The font, font color and background color for the third row of text in the template.
/// The third row is used to display store name or the default tertiary text.
#pragma mark - Tertiary Text

/// Tertiary text font. Expects a UIFont.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyTertiaryFont;

/// Tertiary text font color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyTertiaryFontColor;

/// Tertiary text background color. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyTertiaryBackgroundColor;

#pragma mark - Additional Style Options

/// The background color for the bulk of the ad. Expects a UIColor.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyMainBackgroundColor;

/// The corner rounding radius for the icon view and call to action. Expects an NSNumber.
extern GADTNativeTemplateStyleKey const GADTNativeTemplateStyleKeyCornerRadius;

/// The super class for every template object.
/// This class has the majority of all layout and styling logic.
@interface GADTTemplateView : GADUnifiedNativeAdView
@property(nonatomic, copy) NSDictionary<GADTNativeTemplateStyleKey, NSObject*>* styles;
@property(weak) IBOutlet UILabel* primaryTextView;
@property(weak) IBOutlet UILabel* secondaryTextView;
@property(weak) IBOutlet UILabel* tertiaryTextView;
@property(weak) IBOutlet UILabel* adBadge;
@property(weak) IBOutlet UIImageView* brandImage;
@property(weak) IBOutlet UIView* backgroundView;
@property(weak) UIView* rootView;

/// Adds a constraint to the superview so that the template spans the width of its parent.
/// Does nothing if there is no superview.
- (void)addHorizontalConstraintsToSuperviewWidth;

/// Adds a constraint to the superview so that the template is centered vertically in its parent.
/// Does nothing if there is no superview.
- (void)addVerticalCenterConstraintToSuperview;

/// Utility method to get a color from a hex string.
+ (UIColor*)colorFromHexString:(NSString*)hexString;

- (NSString *)getTemplateTypeName;
@end
